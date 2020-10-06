package eu.toop.dsd.api;

import org.junit.Test;

import java.io.IOException;

public class ToopDirClientTest {

  public static final String TOOP_DIR_URL = "http://directory.acc.exchange.toop.eu";

  @Test
  public void searchCountry() throws IOException {
    final String result = ToopDirClient.callSearchApiWithCountryCode(TOOP_DIR_URL, "SV");
    System.out.println(result);
  }

  @Test
  public void searchIdentifierScheme() throws IOException {
    final String result = ToopDirClient.callSearchApiWithIdentifierScheme(TOOP_DIR_URL, "DataSubectIdentifierScheme");
    System.out.println(result);
  }
}