package org.api.service.model;

import java.io.Serializable;

public class Visit implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private Long time;
  private String ipAddress;
  private String country;

  public Visit() {}

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
    StringBuilder builder = new StringBuilder();
    builder
        .append("Visit [time=")
        .append(time)
        .append(", ipAddress=")
        .append(ipAddress)
        .append(", country=")
        .append(country)
        .append("]");
    return builder.toString();
  }

  public static class Builder {
    private Long time;
    private String ipAddress;
    private String country;

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

    public Visit build() {
      return new Visit(this);
    }
  }

  private Visit(Builder builder) {
    this.time = builder.time;
    this.ipAddress = builder.ipAddress;
    this.country = builder.country;
  }
}
