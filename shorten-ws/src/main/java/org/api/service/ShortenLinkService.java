package org.api.service;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.api.persistence.LinkRepository;
import org.api.persistence.VisitRepository;
import org.api.persistence.model.LinkEntity;
import org.api.persistence.model.VisitEntity;
import org.api.service.exception.InvalidURLException;
import org.api.service.exception.LinkNotFoundException;
import org.api.service.model.Link;
import org.api.service.model.UpdatedLink;
import org.api.service.model.Visit;
import org.api.service.model.validators.ValidURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.vavr.CheckedFunction1;

@Service
public class ShortenLinkService {

  @Autowired private LinkRepository linkRepository;
  @Autowired private VisitRepository visitRepository;

  private static final Logger LOG = LoggerFactory.getLogger(ShortenLinkService.class);
  private final Function<String, String> mapperURLTemplate =
      str -> String.format("http://shortweb.ly/%s", str);

  private Optional<CircuitBreaker> customCircuitBreaker = Optional.empty();
  private Optional<RateLimiter> customRateLimiter = Optional.empty();

  private CircuitBreaker customCircuitBreaker() {
    if (customCircuitBreaker.isPresent()) return customCircuitBreaker.get();
    CircuitBreakerConfig circuitBreakerConfig =
        CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(10000))
            .ringBufferSizeInHalfOpenState(2)
            .ringBufferSizeInClosedState(2)
            .ignoreExceptions(InvalidURLException.class, LinkNotFoundException.class)
            .build();
    CircuitBreaker circuitBreaker = CircuitBreaker.of("shortenservice", circuitBreakerConfig);
    circuitBreaker
        .getEventPublisher()
        .onError(
            event ->
                LOG.info(
                    "Identified error on circuit breaker this may change the state {}", event));
    circuitBreaker
        .getEventPublisher()
        .onStateTransition(event -> LOG.info("Circuit breaker state transitioned {}", event));
    this.customCircuitBreaker = Optional.of(circuitBreaker);
    return circuitBreaker;
  }

  private RateLimiter customRateLimiter() {
    if (customRateLimiter.isPresent()) return customRateLimiter.get();
    RateLimiterConfig config =
        new RateLimiterConfig.Builder()
            .limitRefreshPeriod(Duration.ofMillis(60000))
            .limitForPeriod(10)
            .timeoutDuration(Duration.ofMillis(25))
            .build();
    RateLimiter rateLimiter = RateLimiter.of("visitlimiter", config);
    rateLimiter
        .getEventPublisher()
        .onFailure(
            event ->
                LOG.info("Identified error on rate limiter this may change the state {}", event))
        .onSuccess(event -> LOG.info("Success on rate limiter {}", event));
    this.customRateLimiter = Optional.of(rateLimiter);
    return rateLimiter;
  }

  public Function<String, Link> shorten() {
    return CircuitBreaker.decorateFunction(
        customCircuitBreaker(),
        urlString -> {
          LOG.info("Received request to shorten: {}", urlString);
          ValidURL url = ValidURL.create(urlString);
          String randomString = RandomStringUtils.random(10, true, true);
          if (!url.isValid()) {
            throw new InvalidURLException();
          }
          LinkEntity link =
              new LinkEntity.Builder().mainLink(url.url()).shortenLink(randomString).build();
          link = linkRepository.save(link);
          String newMapperURL = mapperURLTemplate.apply(randomString);
          ValidURL mappedURL = ValidURL.create(newMapperURL);
          if (!mappedURL.isValid()) {
            throw new InvalidURLException();
          }
          LOG.info("Link shortened to: {}", link);
          return Optional.of(link)
              .map(
                  l ->
                      new Link.Builder()
                          .id(l.getId())
                          .link(l.getMainLink())
                          .mappedLink(mappedURL.url())
                          .build())
              .orElse(null);
        });
  }

  public Function<String, Function<String, UpdatedLink>> updateShortenedLink() {

    return CircuitBreaker.decorateFunction(
        customCircuitBreaker(),
        identifier -> {
          LOG.info("Received request to rename link in id {}", identifier);
          LOG.info("Searching link for provided id {}", identifier);
          final LinkEntity link =
              linkRepository.findById(identifier).orElseThrow(LinkNotFoundException::new);

          return CircuitBreaker.decorateFunction(
              customCircuitBreaker(),
              newShortenedLink -> {
                String oldMapping = link.getShortenLink();
                String newMapperURL = mapperURLTemplate.apply(newShortenedLink);
                ValidURL mappedURLNew = ValidURL.create(newMapperURL);
                if (!mappedURLNew.isValid()) {
                  throw new InvalidURLException();
                }
                link.setShortenLink(newShortenedLink);
                LinkEntity newLink = linkRepository.save(link);
                String oldMapperURL = mapperURLTemplate.apply(oldMapping);
                ValidURL mappedURLOld = ValidURL.create(oldMapperURL);
                LOG.info(
                    "Renamed link for provided id {} from old map{} to new map {}.",
                    identifier,
                    oldMapperURL,
                    newMapperURL);
                if (!mappedURLOld.isValid()) {
                  throw new InvalidURLException();
                }
                return Optional.of(newLink)
                    .map(
                        l ->
                            new UpdatedLink.Builder()
                                .link(l.getMainLink())
                                .oldMappedLink(mappedURLOld.url())
                                .newMappedLink(mappedURLNew.url())
                                .build())
                    .orElse(null);
              });
        });
  }

  public Supplier<Map<String, String>> allLinks() {
    return CircuitBreaker.decorateSupplier(
        customCircuitBreaker(),
        () -> {
          Map<String, String> response = new HashMap<>();
          LOG.info("Request received to retrieve all links.");
          linkRepository
              .findAll()
              .forEach(
                  l -> {
                    response.put(
                        l.getMainLink().toString(), mapperURLTemplate.apply(l.getShortenLink()));
                  });
          return response;
        });
  }

  public Function<String, List<Visit>> visits() {
    return CircuitBreaker.decorateFunction(
        customCircuitBreaker(),
        shortenLink -> {
          LOG.info(
              "Request received to retrieve visits of link with shorten link {}.", shortenLink);
          LinkEntity entity =
              Optional.ofNullable(linkRepository.findLinkByShortenLink(shortenLink))
                  .orElseThrow(LinkNotFoundException::new);

          List<VisitEntity> visits =
              Optional.ofNullable(visitRepository.findVisitByLink(entity.getId()))
                  .orElse(Arrays.asList());

          return visits
              .stream()
              .map(
                  visit ->
                      new Visit.Builder()
                          .time(visit.getTime())
                          .country(visit.getCountry())
                          .ipAddress(visit.getIpAddress())
                          .build())
              .collect(Collectors.toList());
        });
  }

  public Function<String, CheckedFunction1<HttpServletRequest, RedirectView>> redirect() {

    Function<String, CheckedFunction1<HttpServletRequest, RedirectView>> circuitBreakedCall =
        CircuitBreaker.decorateFunction(
            customCircuitBreaker(),
            shortenLink -> {
              LOG.info(
                  "Request received to visit(redirect) link with shorten link {}.", shortenLink);
              final LinkEntity entity =
                  Optional.ofNullable(linkRepository.findLinkByShortenLink(shortenLink))
                      .orElseThrow(LinkNotFoundException::new);
              return CircuitBreaker.decorateCheckedFunction(
                  customCircuitBreaker(),
                  request -> {
                    URL url = entity.getMainLink();
                    String country = Country.find(request.getRemoteAddr());
                    VisitEntity visit =
                        new VisitEntity.Builder()
                            .ipAddress(request.getRemoteAddr())
                            .country(country)
                            .time(Instant.now().getEpochSecond())
                            .link(entity)
                            .build();

                    visitRepository.save(visit);
                    LOG.info("This visit metadata for {} is saved as {}.", shortenLink, visit);
                    RedirectView redirectView = new RedirectView();
                    redirectView.setUrl(url.toExternalForm());
                    return redirectView;
                  });
            });
    return RateLimiter.decorateFunction(customRateLimiter(), circuitBreakedCall);
  }

  public void redirect(String shortenLink) {
    throw new LinkNotFoundException();
  }
}
