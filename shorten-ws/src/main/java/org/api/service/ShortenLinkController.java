package org.api.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.api.service.exception.InvalidURLException;
import org.api.service.exception.LinkNotFoundException;
import org.api.service.exception.MaintainenceException;
import org.api.service.exception.TooManyRequestException;
import org.api.service.model.Link;
import org.api.service.model.UpdatedLink;
import org.api.service.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.github.resilience4j.circuitbreaker.CircuitBreakerOpenException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.control.Try;

@RestController
public class ShortenLinkController implements ShortenLink {

  @Autowired ShortenLinkService service;

  private Function<Throwable, RuntimeException> circuitBreakerOpenException =
      ex -> {
        if (ex instanceof CircuitBreakerOpenException) return new MaintainenceException();
        if (ex instanceof LinkNotFoundException) return new LinkNotFoundException();
        if (ex instanceof InvalidURLException) return new InvalidURLException();
        if (ex instanceof RequestNotPermitted) return new TooManyRequestException();
        return new RuntimeException(ex);
      };

  @Override
  @Transactional
  @RequestMapping(
    path = "/create",
    method = RequestMethod.POST,
    consumes = MediaType.TEXT_PLAIN_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Link shorten(@RequestBody String urlString) {
    return Try.of(() -> service.shorten().apply(urlString))
        .getOrElseThrow(circuitBreakerOpenException);
  }

  @Override
  @RequestMapping(path = "/create/{id}", method = RequestMethod.POST)
  public UpdatedLink updateShortenedLink(
      @PathVariable(name = "id") String id,
      @RequestParam(name = "newLinkSuffix") String newShortenedLink) {
    return Try.of(() -> service.updateShortenedLink().apply(id).apply(newShortenedLink))
        .getOrElseThrow(circuitBreakerOpenException);
  }

  @Override
  @RequestMapping("/list")
  public Map<String, String> allLinks() {
    return Try.of(() -> service.allLinks().get()).getOrElseThrow(circuitBreakerOpenException);
  }

  @Override
  @RequestMapping("/visits/{shortenlink}")
  public List<Visit> visits(@PathVariable(name = "shortenlink") String shortenLink) {
    return Try.of(() -> service.visits().apply(shortenLink))
        .getOrElseThrow(circuitBreakerOpenException);
  }

  @RequestMapping("/{shortenlink}")
  public RedirectView redirect(
      @PathVariable(name = "shortenlink") String shortenLink, HttpServletRequest request)
      throws Throwable {
    return Try.of(() -> service.redirect().apply(shortenLink).apply(request))
        .getOrElseThrow(circuitBreakerOpenException);
  }

  @Override
  public void redirect(String shortenLink) {
    throw new LinkNotFoundException();
  }
}
