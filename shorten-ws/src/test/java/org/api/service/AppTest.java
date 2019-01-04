package org.api.service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.api.persistence.LinkRepository;
import org.api.persistence.VisitRepository;
import org.api.service.model.Link;
import org.api.service.model.UpdatedLink;
import org.api.service.model.Visit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppTest {

  @Autowired VisitRepository visitRepos;
  @Autowired LinkRepository linkRepos;

  private static final String CREATE = "/create";
  private static final String VALID_MAIN_LINK = "http://www.google.com";
  private static final String VALID_MAIN_LINK2 = "http://www.yahoo.com";
  private static final String VALID_MAIN_LINK3 = "http://mail.google.com";
  private static final String INVALID_MAIN_LINK = "aksjd";

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void createLinkTest() {
    cleanAll();
    Link result = restTemplate.postForEntity(CREATE, VALID_MAIN_LINK, Link.class).getBody();
    System.out.println(result);
    Assert.assertTrue(result != null);
    Assert.assertTrue(result.getId() != null);
    Assert.assertTrue(result.getMappedLink() != null);
    Assert.assertEquals(VALID_MAIN_LINK, result.getLink().toExternalForm());
  }

  @Test
  public void createLinkInvalidTest() {
    cleanAll();
    visitRepos.deleteAll();
    ResponseEntity<Link> result = restTemplate.postForEntity(CREATE, INVALID_MAIN_LINK, Link.class);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  public void createLinkAndCheckVisits() {
    cleanAll();
    Link link = restTemplate.postForEntity(CREATE, VALID_MAIN_LINK, Link.class).getBody();
    System.out.println(link);
    ResponseEntity<String> result =
        restTemplate.getForEntity(
            String.format("/%s", link.getMappedLink().getPath()), String.class);
    Assert.assertEquals(HttpStatus.OK, result.getStatusCode());

    List<Visit> visits =
        restTemplate
            .exchange(
                URI.create(
                    String.format("/visits/%s", link.getMappedLink().getPath().substring(1))),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Visit>>() {})
            .getBody();
    Assert.assertEquals(1, visits.size());
  }

  @Test
  public void checkVisitsOfUnavailableLink() {
    cleanAll();
    ResponseEntity<String> result =
        restTemplate.getForEntity(String.format("/%s", "google"), String.class);
    Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    ResponseEntity<String> visits =
        restTemplate.exchange(
            URI.create(String.format("/visits/%s", "google")), HttpMethod.GET, null, String.class);
    System.out.println(visits.getBody());
    Assert.assertEquals(HttpStatus.NOT_FOUND, visits.getStatusCode());
  }

  @Test
  public void renameLink() {
    cleanAll();
    Link result = restTemplate.postForEntity(CREATE, VALID_MAIN_LINK, Link.class).getBody();

    UpdatedLink link =
        restTemplate
            .postForEntity(
                String.format("%s/%s?newLinkSuffix=%s", CREATE, result.getId(), "mygoogle"),
                null,
                UpdatedLink.class)
            .getBody();
    Assert.assertEquals("/mygoogle", link.getNewMappedLink().getPath());
  }

  @Test
  public void renameLinkWithWrongId() {
    cleanAll();

    ResponseEntity<UpdatedLink> linkResponse =
        restTemplate.postForEntity(
            String.format("%s/%s?newLinkSuffix=%s", CREATE, "wxyz", "mygoogle"),
            null,
            UpdatedLink.class);
    Assert.assertEquals(HttpStatus.NOT_FOUND, linkResponse.getStatusCode());
  }

  @Test
  public void listAllLinks() {
    cleanAll();
    restTemplate.postForEntity(CREATE, VALID_MAIN_LINK, Link.class).getBody();
    restTemplate.postForEntity(CREATE, VALID_MAIN_LINK2, Link.class).getBody();
    restTemplate.postForEntity(CREATE, VALID_MAIN_LINK3, Link.class).getBody();

    Map<String, String> result =
        restTemplate
            .exchange(
                URI.create("/list"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {})
            .getBody();
    Assert.assertEquals(3, result.size());
  }

  private void cleanAll() {
    visitRepos.deleteAll();
    linkRepos.deleteAll();
  }
}
