/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.dsd.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.*;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.SimpleURL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the bridge between DSD and TOOP directory. It queries the TOOP
 * directory and returns the responses as a list of <code>MatchType</code>
 * objects
 *
 * @author yerlibilgin
 */
public class ToopDirClient {

  public static void disableSSLVerification() {

    TrustManager[] trustAllCerts = new TrustManager[]{new X509ExtendedTrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

      }

      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

      }

      @Override
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      @Override
      public void checkClientTrusted(X509Certificate[] certs, String authType) {
      }

      @Override
      public void checkServerTrusted(X509Certificate[] certs, String authType) {
      }

    }};

    SSLContext sc = null;
    try {
      sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (KeyManagementException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }


  static {
    disableSSLVerification();
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(ToopDirClient.class);

  /**
   * Query TOOP-DIR with country code and doctype. Return a String that contains the result
   *
   * @param toopDirBaseURL the base URL of Toop Directory, <code>mandatory</code>
   * @param sCountryCode   two letter Country Code, <code>mandatory</code>
   * @return a String that contains the result
   * @throws IOException if a communication problem occurs
   */
  public static String callSearchApiWithCountryCode(final String toopDirBaseURL, @Nullable final String sCountryCode) throws IOException {
    ValueEnforcer.notEmpty(toopDirBaseURL, "toopDirBaseURL");
    ValueEnforcer.notEmpty(sCountryCode, "sCountryCode");

    // Build base URL and fetch all records per HTTP request
    final SimpleURL aBaseURL = new SimpleURL(toopDirBaseURL + "/search/1.0/xml");
    // More than 1000 is not allowed
    aBaseURL.add("rpc", 100);

    // Parameters to this servlet
    if (sCountryCode != null && !sCountryCode.isEmpty()) {
      aBaseURL.add("country", sCountryCode);
    }

    return callSearchApi(aBaseURL);
  }

  /**
   * Query TOOP-DIR with dpType. Return a String that contains the result
   *
   * @param toopDirBaseURL the base URL of Toop Directory, <code>mandatory</code>
   * @param dpType         data provider type, <code>mandatory</code>
   * @return a String that contains the result
   * @throws IOException if a communication problem occurs
   */
  public static String callSearchApiForDpType(@Nonnull final String toopDirBaseURL,
                                              @Nonnull final String dpType) throws IOException {
    ValueEnforcer.notEmpty(toopDirBaseURL, toopDirBaseURL);
    ValueEnforcer.notEmpty(dpType, dpType);

    if (StringHelper.hasNoText(toopDirBaseURL))
      throw new IllegalStateException("The Directory base URL configuration is missing");

    // Build base URL and fetch all records per HTTP request
    final SimpleURL aBaseURL = new SimpleURL(toopDirBaseURL + "/search/1.0/xml");
    // More than 1000 is not allowed
    aBaseURL.add("rpc", 100);

    if (dpType != null && !dpType.isEmpty()) {
      aBaseURL.add("identifierValue", dpType);
    }


    return callSearchApi(aBaseURL);
  }


  private static String callSearchApi(SimpleURL aBaseURL) throws IOException {
    if (LOGGER.isInfoEnabled())
      LOGGER.info("Querying " + aBaseURL.getAsStringWithEncodedParameters());

    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      final HttpGet aGet = new HttpGet(aBaseURL.getAsURI());

      final HttpResponse response = httpClient.execute(aGet);

      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw new IllegalStateException("Request failed " + response.getStatusLine().getStatusCode());
      }

      try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
        response.getEntity().writeTo(stream);
        final byte[] s_bytes = stream.toByteArray();

        final String s_result = new String(s_bytes, StandardCharsets.UTF_8);
        return s_result;
      }
    }
  }

}
