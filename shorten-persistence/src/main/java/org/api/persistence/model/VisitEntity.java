package org.api.persistence.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "visit")
@Table(name = "visit")
public class VisitEntity implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "link_id")
  private LinkEntity link;

  private Long time;
  private String ipAddress;
  private String country;

  public VisitEntity() {}

  /** @return the id */
  public Long getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(Long id) {
    this.id = id;
  }

  /** @return the link */
  public LinkEntity getLink() {
    return link;
  }

  /** @param link the link to set */
  public void setLink(LinkEntity link) {
    this.link = link;
  }

  /** @return the time */
  public Long getTime() {
    return time;
  }

  /** @param time the time to set */
  public void setTime(Long time) {
    this.time = time;
  }

  /** @return the ipAddress */
  public String getIpAddress() {
    return ipAddress;
  }

  /** @param ipAddress the ipAddress to set */
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  /** @return the country */
  public String getCountry() {
    return country;
  }

  /** @param country the country to set */
  public void setCountry(String country) {
    this.country = country;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder2 = new StringBuilder();
    builder2
        .append("Visit [id=")
        .append(id)
        .append(", link=")
        .append(link)
        .append(", time=")
        .append(time)
        .append(", ipAddress=")
        .append(ipAddress)
        .append(", country=")
        .append(country)
        .append("]");
    return builder2.toString();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
    result = prime * result + ((link == null) ? 0 : link.hashCode());
    result = prime * result + ((time == null) ? 0 : time.hashCode());
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
    VisitEntity other = (VisitEntity) obj;
    if (country == null) {
      if (other.country != null) return false;
    } else if (!country.equals(other.country)) return false;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (ipAddress == null) {
      if (other.ipAddress != null) return false;
    } else if (!ipAddress.equals(other.ipAddress)) return false;
    if (link == null) {
      if (other.link != null) return false;
    } else if (!link.equals(other.link)) return false;
    if (time == null) {
      if (other.time != null) return false;
    } else if (!time.equals(other.time)) return false;
    return true;
  }

  public static class Builder {
    private Long id;
    private LinkEntity link;
    private Long time;
    private String ipAddress;
    private String country;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder link(LinkEntity link) {
      this.link = link;
      return this;
    }

    public Builder time(Long time) {
      this.time = time;
      return this;
    }

    public Builder ipAddress(String ipAddress) {
      this.ipAddress = ipAddress;
      return this;
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    public VisitEntity build() {
      return new VisitEntity(this);
    }
  }

  private VisitEntity(Builder builder) {
    this.id = builder.id;
    this.link = builder.link;
    this.time = builder.time;
    this.ipAddress = builder.ipAddress;
    this.country = builder.country;
  }
}
