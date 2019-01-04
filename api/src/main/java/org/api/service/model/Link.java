package org.api.service.model;

import java.io.Serializable;
import java.net.URL;

public class Link implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private String id;
  private URL link;
  private URL mappedLink;

  public Link() {}

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the link */
  public URL getLink() {
    return link;
  }

  /** @param link the link to set */
  public void setLink(URL link) {
    this.link = link;
  }

  /** @return the mappedLink */
  public URL getMappedLink() {
    return mappedLink;
  }

  /** @param mappedLink the mappedLink to set */
  public void setMappedLink(URL mappedLink) {
    this.mappedLink = mappedLink;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder2 = new StringBuilder();
    builder2
        .append("Link [id=")
        .append(id)
        .append(", link=")
        .append(link)
        .append(", mappedLink=")
        .append(mappedLink)
        .append("]");
    return builder2.toString();
  }

  public static class Builder {
    private String id;
    private URL link;
    private URL mappedLink;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder link(URL link) {
      this.link = link;
      return this;
    }

    public Builder mappedLink(URL mappedLink) {
      this.mappedLink = mappedLink;
      return this;
    }

    public Link build() {
      return new Link(this);
    }
  }

  private Link(Builder builder) {
    this.id = builder.id;
    this.link = builder.link;
    this.mappedLink = builder.mappedLink;
  }
}
