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
import com.helger.pd.searchapi.v1.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


/**
 * This class is the main query processor for the DSD queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP DSD specifications.
 *
 * @author Muhammet Yildiz
 */

public class DSDQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDQueryService.class);
  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";

  public static final String PARAM_NAME_DATA_SET_TYPE = "DataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  private static final String PARAM_NAME_COUNTRY_CODE = "countryCode";
  private static final String PARAM_NAME_DATA_PROVIDER_TYPE = "dataProviderType";


  /**
   * Query the underlying database for the provided parameters and
   * respond using the TOOP DSD RegRep response specification.
   *
   * @param parameterMap   the map that contains the parameters for the queries
   * @param responseStream the stream to write the results in case of success.
   * @throws IllegalArgumentException if the query parameters are invalid
   * @throws IllegalStateException    if a problem occurs
   */
  public static void processRequest(Map<String, String[]> parameterMap, OutputStream responseStream) {
    ValueEnforcer.notEmpty(parameterMap, "parameterMap");

    String s_QueryId = null;

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

  public static void processDataSetRequest(Map<String, String[]> parameterMap, OutputStream responseStream) throws IOException {

    String s_DataSetType;
    String s_CountryCode = null;
    String s_DataProviderType = null;

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

    String[] dataProviderType = parameterMap.get(PARAM_NAME_DATA_PROVIDER_TYPE);
    if (dataProviderType != null) {
      if (dataProviderType.length != 1) {
        throw new IllegalStateException("dataProviderType invalid");
      }

      s_DataProviderType = dataProviderType[0];
    }


    LOGGER.debug("Processing data set request [dataSetType: " + s_DataSetType +
        ", countryCode: " + s_CountryCode + ", dataProviderType: " + s_DataProviderType + "]");

    LOGGER.warn("Ingoring \"dataProviderType\":" + dataProviderType + " parameter for now");


    final List<MatchType> matchTypes = ToopDirClient.performSearch(s_CountryCode, s_DataSetType);

    List<Document> dcatDocuments = BregDCatHelper.convertBusinessCardsToDCat(matchTypes);

    String sRequestId = "0DCFE9A5-3D4E-493A-BC50-C403EF281318"; //todo: get it from the request

    String resultXml = DSDRegRep.createQueryResponse(sRequestId, dcatDocuments);
    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }
}
