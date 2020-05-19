package eu.toop.dsd.client;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.url.SimpleURL;
import com.helger.pd.searchapi.v1.MatchType;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DSDClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDClient.class);

  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";
  public static final String PARAM_NAME_DATA_SET_TYPE = "dataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_COUNTRY_CODE = "countryCode";

  /**
   * The default DSD query as described here: http://wiki.ds.unipi.gr/display/TOOPSA20/Data+Services+Directory
   *
   * @param dsdBaseUrl  the URL where the DSD service resides, may not be null
   * @param datasetType the dataset type, may not be null
   * @param countryCode the country code, optional
   * @return the list of {@link MatchType} objects.
   */
  public static List<MatchType> queryDataset(@Nonnull String dsdBaseUrl, @Nonnull String datasetType,
                                      @Nullable String countryCode) {

    ValueEnforcer.notEmpty(datasetType, "datasetType");

    final SimpleURL aBaseURL = new SimpleURL(dsdBaseUrl + "/rest/search");

    aBaseURL.add(PARAM_NAME_QUERY_ID, QUERY_DATASET_REQUEST);
    aBaseURL.add(PARAM_NAME_DATA_SET_TYPE, datasetType);

    // Parameters to this servlet
    if (countryCode != null && !countryCode.isEmpty()) {
      aBaseURL.add(PARAM_NAME_COUNTRY_CODE, countryCode);
    }

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
        LOGGER.debug(s_result);
        return DsdResponseReader.matchTypeListReader().read(s_result);
      }
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
      throw new IllegalStateException(ex);
    }
  }
}
