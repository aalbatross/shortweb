package org.api.persistence;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.api.persistence.model.LinkEntity;
import org.api.persistence.model.VisitEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LinkTest {

  @Autowired LinkRepository linkRepo;
  @Autowired VisitRepository visitRepo;

  @Test
  public void createLink() throws MalformedURLException {
    String randomString = RandomStringUtils.random(10, true, true);
    LinkEntity l =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.xml.com"))
            .shortenLink(randomString)
            .build();

    linkRepo.save(l);

    linkRepo.findAll().forEach(System.out::println);
  }

  @Test
  public void updateLinkMainLink() throws MalformedURLException {
    String randomString = RandomStringUtils.random(10, true, true);
    LinkEntity l =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.xml.com"))
            .shortenLink(randomString)
            .build();
    linkRepo.save(l);
    l.setMainLink(new URL("http://www.wml.com"));
    linkRepo.save(l);

    linkRepo.findAll().forEach(System.out::println);
  }

  @Test
  public void updateLinkShortLink() throws MalformedURLException {
    String randomString = RandomStringUtils.random(10, true, true);
    LinkEntity l =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.xml.com"))
            .shortenLink(randomString)
            .build();
    linkRepo.save(l);
    l.setShortenLink("mylink");
    linkRepo.save(l);

    linkRepo.findAll().forEach(System.out::println);
  }

  @Test
  public void updateLinkWithNewVisit() throws MalformedURLException {
    String randomString = RandomStringUtils.random(10, true, true);
    LinkEntity l =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.xml.com"))
            .shortenLink(randomString)
            .build();
    linkRepo.save(l);
    VisitEntity v =
        new VisitEntity.Builder()
            .country("INDIA")
            .ipAddress("127.0.0.1")
            .time(Instant.now().getEpochSecond())
            .link(l)
            .build();
    visitRepo.save(v);
    visitRepo.findAll().forEach(System.out::println);
  }

  @Test
  public void readVisitsFromLink() throws MalformedURLException {
    String randomString = RandomStringUtils.random(10, true, true);
    String randomString2 = RandomStringUtils.random(10, true, true);

    LinkEntity l =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.xml.com"))
            .shortenLink(randomString)
            .build();
    VisitEntity v =
        new VisitEntity.Builder()
            .country("INDIA")
            .ipAddress("127.0.0.1")
            .time(Instant.now().getEpochSecond())
            .link(l)
            .build();
    linkRepo.save(l);
    visitRepo.save(v);

    LinkEntity l1 =
        new LinkEntity.Builder()
            .mainLink(new URL("http://www.wml.com"))
            .shortenLink(randomString2)
            .build();
    VisitEntity v1 =
        new VisitEntity.Builder()
            .country("US")
            .ipAddress("127.0.0.1")
            .time(Instant.now().getEpochSecond())
            .link(l1)
            .build();
    linkRepo.save(l1);
    visitRepo.save(v1);

    List<VisitEntity> visits =
        Optional.ofNullable(linkRepo.findLinkByMainLink(new URL("http://www.xml.com")))
            .map(link -> visitRepo.findVisitByLink(link.getId()))
            .orElse(Arrays.asList());
    Assert.assertEquals(1, visits.size());
    Assert.assertEquals("INDIA", visits.get(0).getCountry());
    visits.forEach(System.out::println);
  }
}
