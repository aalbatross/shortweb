package org.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Country {

  private static final String COUNTRY = "country";
  private static final Logger LOG = LoggerFactory.getLogger(Country.class);

  public static String find(String ip) throws IOException {
    URL obj = new URL(String.format("http://ip-api.com/json/%s", ip));
    LOG.debug("IP RECEIVED {}", ip);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");

    int responseCode = con.getResponseCode();
    LOG.debug("GET Response Code :: {}", responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      StringBuffer response = new StringBuffer();
      try (BufferedReader in =
          new BufferedReader(
              new InputStreamReader(con.getInputStream(), Charset.defaultCharset()))) {
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }
      TypeReference<HashMap<String, Object>> typeRef =
          new TypeReference<HashMap<String, Object>>() {};
      HashMap<String, Object> result = new ObjectMapper().readValue(response.toString(), typeRef);
      // print result
      LOG.debug("{}", result.get(COUNTRY));
      return Optional.ofNullable(result.get(COUNTRY)).map(Object::toString).orElse("");
    } else {
      LOG.error("GET request not worked");
      return "";
    }
  }
}
