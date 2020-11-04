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
import javax.xml.transform.TransformerException;

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

import eu.toop.dsd.api.DSDException;
import eu.toop.dsd.api.DsdDataConverter;
import eu.toop.dsd.api.types.DSDQuery;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;

/**
 * This is a helper class that abstracts the rest call to the DSD service for
 * dataset type queries.
 *
 * @author yerlibilgin
 */
public class DSDClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDClient.class);

  private final String m_sDSDBaseURL;
  private HttpClientSettings m_aHttpClientSettings;

  /**
   * Constructor
   *
   * @param dsdBaseUrl the URL where the DSD service resides, may not be
   *                   <code>null</code>
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
   * @param datasetType the dataset type, <code>mandatory</code>
   * @param countryCode the country code, <code>mandatory</code>
   * @return the list of {@link DCatAPDatasetType} objects.
   */
  @Nullable
  public String queryDatasetRawByLocation(@Nonnull final String datasetType, @Nonnull final String countryCode) {
    ValueEnforcer.notEmpty(datasetType, "datasetType");
    ValueEnforcer.notEmpty(countryCode, "countryCode");
    final DSDQuery.DSDQueryID targetQueryId = DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_LOCATION;
    return queryDatasetRaw(datasetType, targetQueryId, DSDQuery.PARAM_NAME_COUNTRY_CODE, countryCode);
  }

  /**
   * The default DSD query as described here:
   * http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param datasetType the dataset type, <code>mandatory</code>
   * @param dpType      the Data Provider Type, <code>mandatory</code>
   * @return the list of {@link DCatAPDatasetType} objects.
   */
  @Nullable
  public String queryDatasetRawByDPType(@Nonnull final String datasetType, @Nonnull final String dpType) {
    ValueEnforcer.notEmpty(datasetType, "datasetType");
    ValueEnforcer.notEmpty(dpType, "dpType");
    final DSDQuery.DSDQueryID targetQueryId = DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_DPTYPE;
    return queryDatasetRaw(datasetType, targetQueryId, DSDQuery.PARAM_NAME_DATA_PROVIDER_TYPE, dpType);
  }

  /**
   * The default DSD query as described here:
   * http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param datasetType the dataset type, <code>mandatory</code>
   * @param countryCode the country code,  <code>mandatory</code>
   * @return the list of {@link DCatAPDatasetType} objects.
   */
  @Nullable
  public List<DCatAPDatasetType> queryDatasetByLocation(@Nonnull final String datasetType,
                                                        @Nonnull final String countryCode) {
    final String result = queryDatasetRawByLocation(datasetType, countryCode);
    return DsdDataConverter.parseDataset(result);
  }

  /**
   * The default DSD query as described here:
   * http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param datasetType the dataset type, <code>mandatory</code>
   * @param dpType      the Data provider type, <code>mandatory</code>
   * @return the list of {@link DCatAPDatasetType} objects.
   */
  @Nullable
  public List<DCatAPDatasetType> queryDatasetByDPType(@Nonnull final String datasetType,
                                                      @Nonnull final String dpType) {
    final String result = queryDatasetRawByDPType(datasetType, dpType);
    return DsdDataConverter.parseDataset(result);
  }

  private String queryDatasetRaw(@Nonnull final String datasetType, final DSDQuery.DSDQueryID targetQueryId,
                                 final String secondParamName, final String secondParam) {

    final SimpleURL aBaseURL = new SimpleURL(m_sDSDBaseURL + "/rest/search");

    aBaseURL.add(DSDQuery.PARAM_NAME_QUERY_ID, targetQueryId.id);
    aBaseURL.add(DSDQuery.PARAM_NAME_DATA_SET_TYPE, datasetType);

    aBaseURL.add(secondParamName, secondParam);
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
          final byte[] bytes = stream.toByteArray();
          final String result = new String(bytes, StandardCharsets.UTF_8);
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DSD result:\n" + result);
          }
          return result;
        }
      }
    } catch (final RuntimeException ex) {
      throw ex;
    } catch (final Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
      throw new DSDException(ex.getMessage(), ex);
    }
  }
}
