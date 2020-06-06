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
package eu.toop.dsd.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.helger.commons.annotation.Nonempty;
import com.helger.pd.searchapi.v1.ResultListType;
import eu.toop.dsd.client.DsdResponseWriter;
import eu.toop.dsd.config.DSDConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.pd.searchapi.v1.MatchType;

import javax.annotation.Nonnull;


/**
 * This class is the main query processor for the DSD queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP DSD specifications.
 *
 * @author yerlibilgin
 */
public class DSDQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDQueryService.class);
  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";

  public static final String PARAM_NAME_DATA_SET_TYPE = "dataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_COUNTRY_CODE = "countryCode";


  /**
   * Query the underlying database for the provided parameters and
   * respond using the TOOP DSD RegRep response specification.
   *
   * @param parameterMap   the map that contains the parameters for the queries, may not be null
   * @param responseStream the stream to write the results in case of success, may not be null
   * @throws IllegalArgumentException if the query parameters are invalid
   * @throws IllegalStateException    if a problem occurs
   */
  public static void processRequest(@Nonnull @Nonempty Map<String, String[]> parameterMap, @Nonnull OutputStream responseStream) {
    ValueEnforcer.notNull(parameterMap, "parameterMap");
    ValueEnforcer.notNull(responseStream, "responseStream");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");


    String s_QueryId;

    //try to get the queryId from the map
    String[] queryId = parameterMap.get(PARAM_NAME_QUERY_ID);
    ValueEnforcer.notEmpty(queryId, "queryId");
    if (queryId.length != 1)
      throw new IllegalStateException("queryId invalid");

    s_QueryId = queryId[0];

    LOGGER.debug("Processing query: [QueryId: " + s_QueryId + "");

    //currently only one type of query is supported
    switch (s_QueryId) {
      case QUERY_DATASET_REQUEST: {
        try {
          processDataSetRequest(parameterMap, responseStream);
        } catch (IOException e) {
          throw new IllegalStateException(e.getMessage(), e);
        }

        break;
      }

      default: {
        throw new IllegalArgumentException("Invalid queryId " + queryId);
      }
    }
  }

  /**
   * Processes the incoming parameter map as a dataset request parameter map and performs a dataset request.
   * @param parameterMap the map that contains the parameters to the query. May not be null
   * @param responseStream the result will be written into this stream
   * @throws IOException if an io problem occurs.
   */
  public static void processDataSetRequest(@Nonnull @Nonempty Map<String, String[]> parameterMap, @Nonnull  OutputStream responseStream) throws IOException {
    ValueEnforcer.notNull(parameterMap, "parameterMap");
    ValueEnforcer.notNull(responseStream, "responseStream");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");

    String s_DataSetType;
    String s_CountryCode = null;

    String[] dataSetType = parameterMap.get(PARAM_NAME_DATA_SET_TYPE);
    ValueEnforcer.notEmpty(dataSetType, "dataSetType");
    if (dataSetType.length != 1)
      throw new IllegalStateException("dataSetType invalid");

    s_DataSetType = dataSetType[0];

    String[] countryCode = parameterMap.get(PARAM_NAME_COUNTRY_CODE);
    if (countryCode != null) {
      if (countryCode.length != 1) {
        throw new IllegalStateException("countryCode invalid");
      }
      s_CountryCode = countryCode[0];
    }

    LOGGER.debug("Processing data set request [dataSetType: " + s_DataSetType +
        ", countryCode: " + s_CountryCode + "]");

    //query all the matches without a document type id.
    final ResultListType resultListType = ToopDirClient.callSearchApi(DSDConfig.getToopDirUrl(), s_CountryCode, null);
    final List<MatchType> matchTypes = resultListType.getMatch();

    StringWriter writer = new StringWriter();
    DsdResponseWriter.matchTypesWriter(s_DataSetType, matchTypes).write(writer);
    String resultXml = writer.toString();

    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }
}
