/**
 * Copyright (C) 2018-2019 toop.eu
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
package eu.toop.dsd.service;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.SimpleURL;
import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;
import eu.toop.dsd.config.DSDConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class is the bridge between DSD and TOOP directory.
 * It queries the TOOP directory and returns the responses as a list of <code>MatchType</code> objects
 * 
 * @author @yerlibilgin
 */
public class ToopDirClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDQueryService.class);

  /**
   * Query TOOP-DIR with country code and doctype. Return the result as a list of <code>MatchType</code> objects
   * 
   * @param sCountryCode two letter Country Code, not null
   * @param aDocumentTypeID doc type id, may be null
   * @return list of <code>MatchType</code> objects
   * @throws IOException if a communication problem occurs
   */
  public static List<MatchType> performSearch(final String sCountryCode,
                                              final String aDocumentTypeID) throws IOException {
    ValueEnforcer.notNull(sCountryCode, "sCountryCode");

    final String sBaseURL = DSDConfig.getToopDirUrl();
    if (StringHelper.hasNoText(sBaseURL))
      throw new IllegalStateException("The Directory base URL configuration is missing");

    // Build base URL and fetch all records per HTTP request
    final SimpleURL aBaseURL = new SimpleURL(sBaseURL + "/search/1.0/xml");
    // More than 1000 is not allowed
    aBaseURL.add("rpc", 1_000);
    // Constant defined in CCTF-103
    aBaseURL.add("identifierScheme", "DataSubjectIdentifierScheme");
    // Parameters to this servlet
    aBaseURL.add("country", sCountryCode);
    if (aDocumentTypeID != null && !aDocumentTypeID.isEmpty())
      aBaseURL.add("doctype", aDocumentTypeID);

    if (LOGGER.isInfoEnabled())
      LOGGER.info("Querying " + aBaseURL.getAsStringWithEncodedParameters());

    HttpClient httpClient = HttpClients.createDefault();

    final HttpGet aGet = new HttpGet(aBaseURL.getAsURI());

    HttpResponse response = httpClient.execute(aGet);

    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
      throw new IllegalStateException("Request failed " + response.getStatusLine().getStatusCode());
    } else {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      response.getEntity().writeTo(stream);
      final String s_result = new String(stream.toByteArray());
      LOGGER.debug(s_result);

      final ResultListType read = PDSearchAPIReader.resultListV1().read(s_result);
      read.getMatch().forEach(match -> {
        LOGGER.debug(match.toString());
      });

      return read.getMatch();
    }

  }


  public static void main(String[] args) {

    try {
      performSearch("GQ", null);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }
}
