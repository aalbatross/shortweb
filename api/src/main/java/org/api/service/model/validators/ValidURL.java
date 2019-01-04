package org.api.service.model.validators;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class ValidURL implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private final URL url;
  private final boolean isValid;

  private ValidURL(URL u, boolean isValid) {
    this.url = u;
    this.isValid = isValid;
  }

  public URL url() {
    return url;
  }

  public boolean isValid() {
    return isValid;
  }

  public static ValidURL create(String url) {
    try {
      URL u = new URL(url);
      return new ValidURL(u, true);
    } catch (MalformedURLException ex) {
      return new ValidURL(null, false);
    }
  }
}
