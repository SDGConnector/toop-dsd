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
package eu.toop.dsd.service;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import eu.toop.dsd.api.DsdResponseWriter;
import eu.toop.dsd.api.ToopDirClient;
import eu.toop.dsd.config.DSDConfig;
import eu.toop.dsd.service.util.DSDQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * This class is the main query processor for the DSD queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP DSD specifications.
 *
 * @author yerlibilgin
 */
public class DSDQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDQueryService.class);

  /**
   * Query the underlying database for the provided parameters and
   * respond using the TOOP DSD RegRep response specification.
   *
   * @param parameterMap   the map that contains the parameters for the queries, may not be null
   * @param responseStream the stream to write the results in case of success, may not be null
   * @throws IllegalArgumentException if the query parameters are invalid
   * @throws IllegalStateException    if a problem occurs
   */
  public static void processRequest(@Nonnull @Nonempty Map<String, String[]> parameterMap, @Nonnull OutputStream responseStream) throws IOException {
    ValueEnforcer.notNull(parameterMap, "parameterMap");
    ValueEnforcer.notNull(responseStream, "responseStream");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");


    final DSDQuery dsdQuery = DSDQuery.resolve(parameterMap);

    //currently only one type of query is supported
    switch (dsdQuery.getQueryId()) {
      case QUERY_BY_DATASETTYPE_AND_DPTYPE: {
        processDataSetRequestByDPType(dsdQuery, responseStream);
        break;
      }

      case QUERY_BY_DATASETTYPE_AND_LOCATION: {
        processDataSetRequestByLocation(dsdQuery, responseStream);
        break;
      }
    }
  }

  /**
   * Processes the incoming parameter map as a dataset request parameter map and performs a dataset request with respect to
   * <code>urn:toop:dsd:ebxml-regrem:queries:ByDatasetTypeAndDPType</code>
   *
   * @param dsdQuery       the parameters resolved as a {@link DSDQuery} object
   * @param responseStream the result will be written into this stream
   * @throws IOException if an io problem occurs.
   */
  public static void processDataSetRequestByDPType(DSDQuery dsdQuery, OutputStream responseStream) throws IOException {
    ValueEnforcer.notNull(dsdQuery, "dsdQuery");
    ValueEnforcer.notNull(responseStream, "responseStream");


    String dataSetType = dsdQuery.getParameterValue(DSDQuery.PARAM_NAME_DATA_SET_TYPE);
    String dpType = dsdQuery.getParameterValue(DSDQuery.PARAM_NAME_DATA_PROVIDER_TYPE);

    LOGGER.debug("Processing data set request [dataSetType: " + dataSetType +
        ", dpType: " + dpType + "]");

    //query all the matches without a document type id.
    final String directoryResult = ToopDirClient.callSearchApi(DSDConfig.getToopDirUrl(), null, null);

    String resultXml = DsdResponseWriter.convertDIRToDSD(directoryResult);

    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Processes the incoming parameter map as a dataset request parameter map and performs a dataset request with respect to
   * <code>urn:toop:dsd:ebxml-regrem:queries:ByDatasetTypeAndLocation</code>
   *
   * @param dsdQuery       the parameters resolved as a {@link DSDQuery} object
   * @param responseStream the result will be written into this stream
   * @throws IOException if an io problem occurs.
   */
  public static void processDataSetRequestByLocation(@Nonnull DSDQuery dsdQuery, @Nonnull OutputStream responseStream) throws IOException {
    ValueEnforcer.notNull(dsdQuery, "dsdQuery");
    ValueEnforcer.notNull(responseStream, "responseStream");


    String dataSetType = dsdQuery.getParameterValue(DSDQuery.PARAM_NAME_DATA_SET_TYPE);
    String countryCode = dsdQuery.getParameterValue(DSDQuery.PARAM_NAME_COUNTRY_CODE);

    LOGGER.debug("Processing data set request [dataSetType: " + dataSetType +
        ", countryCode: " + countryCode + "]");

    //query all the matches without a document type id.
    final String directoryResult = ToopDirClient.callSearchApi(DSDConfig.getToopDirUrl(), countryCode, null);

    String resultXml = DsdResponseWriter.convertDIRToDSD(directoryResult);

    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }
}
