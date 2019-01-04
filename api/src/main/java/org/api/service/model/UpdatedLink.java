package org.api.service.model;

import java.net.URL;

public class UpdatedLink {

  private URL link;
  private URL oldMappedLink;
  private URL newMappedLink;

  public UpdatedLink() {}

  private UpdatedLink(URL link, URL oldMappedLink, URL newMappedLink) {
    this.link = link;
    this.oldMappedLink = oldMappedLink;
    this.newMappedLink = newMappedLink;
  }

  public static UpdatedLink create(URL link, URL oldMappedLink, URL newMappedLink) {
    return new UpdatedLink(link, oldMappedLink, newMappedLink);
  }

  /** @return the link */
  public URL getLink() {
    return link;
  }

  /** @param link the link to set */
  public void setLink(URL link) {
    this.link = link;
  }

  /** @return the oldMappedLink */
  public URL getOldMappedLink() {
    return oldMappedLink;
  }

  /** @param oldMappedLink the oldMappedLink to set */
  public void setOldMappedLink(URL oldMappedLink) {
    this.oldMappedLink = oldMappedLink;
  }

  /** @return the newMappedLink */
  public URL getNewMappedLink() {
    return newMappedLink;
  }

  /** @param newMappedLink the newMappedLink to set */
  public void setNewMappedLink(URL newMappedLink) {
    this.newMappedLink = newMappedLink;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("UpdatedLink [link=")
        .append(link)
        .append(", oldMappedLink=")
        .append(oldMappedLink)
        .append(", newMappedLink=")
        .append(newMappedLink)
        .append("]");
    return builder.toString();
  }

  public static class Builder {
    private URL link;
    private URL oldMappedLink;
    private URL newMappedLink;

    public Builder link(URL link) {
      this.link = link;
      return this;
    }

    public Builder oldMappedLink(URL oldMappedLink) {
      this.oldMappedLink = oldMappedLink;
      return this;
    }

    public Builder newMappedLink(URL newMappedLink) {
      this.newMappedLink = newMappedLink;
      return this;
    }

    public UpdatedLink build() {
      return new UpdatedLink(this);
    }
  }

  private UpdatedLink(Builder builder) {
    this.link = builder.link;
    this.oldMappedLink = builder.oldMappedLink;
    this.newMappedLink = builder.newMappedLink;
  }
}
