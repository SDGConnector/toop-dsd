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
package eu.toop.roa.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.helger.commons.annotation.Nonempty;
import com.helger.pd.searchapi.v1.ResultListType;
import eu.toop.dsd.api.DsdResponseWriter;
import eu.toop.dsd.api.ToopDirClient;
import eu.toop.roa.config.ROAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.pd.searchapi.v1.MatchType;

import javax.annotation.Nonnull;


/**
 * This class is the main query processor for the ROA queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP ROA specification <a href="http://wiki.ds.unipi.gr/display/TOOP/.Registry+of+Authorities+v2.1">
 *   http://wiki.ds.unipi.gr/display/TOOP/.Registry+of+Authorities+v2.1</a>.
 *
 * @author yerlibilgin
 */
public class ROAQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ROAQueryService.class);
  public static final String QUERY_DATASET_REQUEST = "urn:toop:roa:ebxml-regrep:queries:DataConsumerByProcedure";

  public static final String PARAM_NAME_DATA_CONSUMER_ID = "DataConsumerId";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_PROCEDURE_ID = "procedureId";


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
   * Processes the incoming parameter map as a dataset request parameter map and performs a data consumer request.
   * @param parameterMap the map that contains the parameters to the query. May not be null
   * @param responseStream the result will be written into this stream
   * @throws IOException if an io problem occurs.
   */
  public static void processDataSetRequest(@Nonnull @Nonempty Map<String, String[]> parameterMap, @Nonnull OutputStream responseStream) throws IOException {
    ValueEnforcer.notNull(parameterMap, "parameterMap");
    ValueEnforcer.notNull(responseStream, "responseStream");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");

    String s_DataConsumerId;
    String s_ProcedureId = null;

    String[] dataConsumerId = parameterMap.get(PARAM_NAME_DATA_CONSUMER_ID);
    ValueEnforcer.notEmpty(dataConsumerId, "dataConsumerId");
    if (dataConsumerId.length != 1)
      throw new IllegalStateException(PARAM_NAME_DATA_CONSUMER_ID + " invalid");

    s_DataConsumerId = dataConsumerId[0];

    String[] procedureId = parameterMap.get(PARAM_NAME_PROCEDURE_ID);
    if (procedureId != null) {
      if (procedureId.length != 1) {
        throw new IllegalStateException("procedureId invalid");
      }
      s_ProcedureId = procedureId[0];
    }

    LOGGER.debug("Processing data consumer request [data consumer id: " + s_DataConsumerId +
        ", procedureId: " + s_ProcedureId + "]");

    //query all the matches without a document type id.
    // TODO: change this according to roa
    //   THE rest is not DSD
    final ResultListType resultListType = ToopDirClient.callSearchApi(ROAConfig.getToopDirUrl(), s_ProcedureId, null);
    final List<MatchType> matchTypes = resultListType.getMatch();

    StringWriter writer = new StringWriter();
    DsdResponseWriter.matchTypesWriter(s_DataConsumerId, matchTypes).write(writer);
    String resultXml = writer.toString();

    responseStream.write(resultXml.getBytes(StandardCharsets.UTF_8));
  }
}
