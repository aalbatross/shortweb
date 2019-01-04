package org.api.service;

import java.util.List;
import java.util.Map;

import org.api.service.model.Link;
import org.api.service.model.UpdatedLink;
import org.api.service.model.Visit;

public interface ShortenLink {

  public Link shorten(String url);

  public UpdatedLink updateShortenedLink(String id, String newShortenedLink);

  public Map<String, String> allLinks();

  public List<Visit> visits(String shortenLink);

  public void redirect(String shortenLink);
}
