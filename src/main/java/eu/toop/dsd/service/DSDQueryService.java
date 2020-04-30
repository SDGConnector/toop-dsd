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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.helger.pd.searchapi.v1.IDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.ValueEnforcer;
import com.helger.pd.searchapi.v1.MatchType;


/**
 * This class is the main query processor for the DSD queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP DSD specifications.
 *
 * @author @yerlibilgin
 */
public class DSDQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDQueryService.class);
  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";

  public static final String PARAM_NAME_DATA_SET_TYPE = "dataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_COUNTRY_CODE = "countryCode";
  public static final String PARAM_NAME_DATA_PROVIDER_TYPE = "dataProviderType";


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

  public static void processDataSetRequest(Map<String, String[]> parameterMap, OutputStream responseStream) throws IOException {
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
    final List<MatchType> matchTypes = ToopDirClient.performSearch(s_CountryCode, null);

    //now filter the matches that contain a part of the datasettype in their Doctypeids.
    filterDirectoryResult(s_DataSetType, matchTypes);

    List<Document> dcatDocuments = BregDCatHelper.convertBusinessCardsToDCat(s_DataSetType, matchTypes);

    String resultXml = DSDRegRep.createQueryResponse(UUID.randomUUID().toString(), dcatDocuments);
    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * This is a tentative approach. We filter out match types as following:<br/>
   * <pre>
   *   for each matchtype
   *     for each doctype of that matchtype
   *       remote the doctype if it does not contain datasetType
   *     if all doctypes were removed
   *        then remove the matchtype
   * </pre>
   * @param matchTypes
   */
  private static void filterDirectoryResult(String datasetType, List<MatchType> matchTypes) {
    //filter
    final Iterator<MatchType> iterator = matchTypes.iterator();

    iterator.forEachRemaining(matchType -> {
      final Iterator<IDType> iterator1 = matchType.getDocTypeID().iterator();
      iterator1.forEachRemaining(idType -> {
        String concatenated = idType.getScheme() + ":" + idType.getValue();

        //TODO: this is so unsafe, we need a better way here.
        if (!concatenated.toLowerCase().contains(datasetType.toLowerCase())){
          iterator1.remove();
        }
      });

      if (matchType.getDocTypeID().size() == 0){
        iterator.remove();
      }
    });
  }
}
