/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.dsd.client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.toop.dsd.api.DSDTypesManipulator;
import eu.toop.dsd.api.DsdResponseReader;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.url.SimpleURL;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.HttpClientSettings;
import com.helger.pd.searchapi.v1.MatchType;

/**
 * This is a helper class that abstracts the rest call to the DSD service for dataset type queries.
 *
 * @author yerlibilgin
 */
public class DSDClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDClient.class);

  /**
   * The constant QUERY_DATASET_REQUEST.
   */
  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";
  /**
   * The constant PARAM_NAME_DATA_SET_TYPE.
   */
  public static final String PARAM_NAME_DATA_SET_TYPE = "dataSetType";
  /**
   * The constant PARAM_NAME_QUERY_ID.
   */
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  /**
   * The constant PARAM_NAME_COUNTRY_CODE.
   */
  public static final String PARAM_NAME_COUNTRY_CODE = "countryCode";

  private final String m_sDSDBaseURL;
  private HttpClientSettings m_aHttpClientSettings;

  /**
   * Constructor
   *
   * @param dsdBaseUrl the URL where the DSD service resides, may not be                   <code>null</code>
   */
  public DSDClient(@Nonnull @Nonempty final String dsdBaseUrl) {
    ValueEnforcer.notEmpty(dsdBaseUrl, "DSD BaseURL");
    m_sDSDBaseURL = dsdBaseUrl;
  }

  /**
   * Sets http client settings.
   *
   * @param aHttpClientSettings the a http client settings
   * @return the http client settings
   */
  @Nonnull
  public DSDClient setHttpClientSettings(@Nullable final HttpClientSettings aHttpClientSettings) {
    m_aHttpClientSettings = aHttpClientSettings;
    return this;
  }

  /**
   * The default DSD query as described here:
   * http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param datasetType the dataset type, may not be <code>null</code>
   * @param countryCode the country code, optional
   * @return the list of {@link DCatAPDatasetType} objects.
   */
  @Nullable
  public List<DCatAPDatasetType> queryDataset(@Nonnull final String datasetType, @Nullable final String countryCode) {

    ValueEnforcer.notEmpty(datasetType, "datasetType");

    final SimpleURL aBaseURL = new SimpleURL(m_sDSDBaseURL + "/rest/search");

    aBaseURL.add(PARAM_NAME_QUERY_ID, QUERY_DATASET_REQUEST);
    aBaseURL.add(PARAM_NAME_DATA_SET_TYPE, datasetType);

    // Parameters to this servlet
    if (countryCode != null && !countryCode.isEmpty()) {
      aBaseURL.add(PARAM_NAME_COUNTRY_CODE, countryCode);
    }

    if (LOGGER.isInfoEnabled())
      LOGGER.info("Querying " + aBaseURL.getAsStringWithEncodedParameters());

    final HttpClientSettings aHttpClientSettings = m_aHttpClientSettings != null ? m_aHttpClientSettings
        : new HttpClientSettings();
    try (final HttpClientManager httpClient = HttpClientManager.create(aHttpClientSettings)) {
      final HttpGet aGet = new HttpGet(aBaseURL.getAsURI());

      try (final CloseableHttpResponse response = httpClient.execute(aGet)) {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
          throw new IllegalStateException("Request failed " + response.getStatusLine().getStatusCode());
        }

        try (final NonBlockingByteArrayOutputStream stream = new NonBlockingByteArrayOutputStream()) {
          response.getEntity().writeTo(stream);
          final byte[] s_bytes = stream.toByteArray();
          if (LOGGER.isDebugEnabled()) {
            final String s_result = new String(s_bytes, StandardCharsets.UTF_8);
            LOGGER.debug("DSD result:\n" + s_result);
          }
          return DsdResponseReader.dcatDatasetTypeReader().read(s_bytes);
        }
      }
    } catch (final RuntimeException ex) {
      throw ex;
    } catch (final Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
      throw new IllegalStateException(ex);
    }
  }

  /**
   * The default DSD query as described here:
   * http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param datasetType the dataset type, may not be <code>null</code>
   * @param countryCode the country code, optional
   * @return the list of {@link MatchType} objects.
   */
  @Nullable
  public List<MatchType> queryDatasetAsMatchTypes(@Nonnull final String datasetType, @Nullable final String countryCode) {
    ValueEnforcer.notEmpty(datasetType, "datasetType");
    final List<DCatAPDatasetType> dCatAPDatasetTypes = queryDataset(datasetType, countryCode);
    return DSDTypesManipulator.convertDCatElementsToMatchTypes(dCatAPDatasetTypes);
  }
}
