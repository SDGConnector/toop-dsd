package eu.toop.dsd.client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

public class DSDClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDClient.class);

  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";
  public static final String PARAM_NAME_DATA_SET_TYPE = "dataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_COUNTRY_CODE = "countryCode";

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
   * @return the list of {@link MatchType} objects.
   */
  @Nullable
  public List<MatchType> queryDataset(@Nonnull final String datasetType, @Nullable final String countryCode) {

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
          return DsdResponseReader.matchTypeListReader().read(s_bytes);
        }
      }
    } catch (final RuntimeException ex) {
      throw ex;
    } catch (final Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
      throw new IllegalStateException(ex);
    }
  }
}
