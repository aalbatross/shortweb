package org.api.persistence.model;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "link")
@Table(name = "link")
public class LinkEntity implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Column(unique = true)
  private URL mainLink;

  @Column(unique = true)
  private String shortenLink;

  public LinkEntity() {}

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the mainLink */
  public URL getMainLink() {
    return mainLink;
  }

  /** @param mainLink the mainLink to set */
  public void setMainLink(URL mainLink) {
    this.mainLink = mainLink;
  }

  /** @return the shortenLink */
  public String getShortenLink() {
    return shortenLink;
  }

  /** @param shortenLink the shortenLink to set */
  public void setShortenLink(String shortenLink) {
    this.shortenLink = shortenLink;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((mainLink == null) ? 0 : mainLink.toExternalForm().hashCode());
    result = prime * result + ((shortenLink == null) ? 0 : shortenLink.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    LinkEntity other = (LinkEntity) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (mainLink == null) {
      if (other.mainLink != null) return false;
    } else if (!mainLink.toExternalForm().equals(other.mainLink.toExternalForm())) return false;
    if (shortenLink == null) {
      if (other.shortenLink != null) return false;
    } else if (!shortenLink.equals(other.shortenLink)) return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder2 = new StringBuilder();
    builder2
        .append("LinkEntity [id=")
        .append(id)
        .append(", mainLink=")
        .append(mainLink)
        .append(", shortenLink=")
        .append(shortenLink)
        .append("]");
    return builder2.toString();
  }

  public static class Builder {
    private String id;
    private URL mainLink;
    private String shortenLink;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder mainLink(URL mainLink) {
      this.mainLink = mainLink;
      return this;
    }

    public Builder shortenLink(String shortenLink) {
      this.shortenLink = shortenLink;
      return this;
    }

    public LinkEntity build() {
      return new LinkEntity(this);
    }
  }

  private LinkEntity(Builder builder) {
    this.id = builder.id;
    this.mainLink = builder.mainLink;
    this.shortenLink = builder.shortenLink;
  }
}
