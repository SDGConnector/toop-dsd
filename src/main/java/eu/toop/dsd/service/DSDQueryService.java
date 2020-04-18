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
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.simple.doctype.SimpleDocumentTypeIdentifier;
import eu.toop.dsd.config.DSDConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;


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

  /**
   * Query the underlying database for the provided parameters and
   * respond using the TOOP DSD RegRep response specification.
   *
   * @param queryId          the queryId provided by the client
   * @param dataSetType      the type of the dataset requested
   * @param countryCode      country code
   * @param dataProviderType the type of the data provider
   * @param resp             Used for writing the query responses directly. (temporary) this parameter will be abstracted from this layer
   */
  public static void processRequest(String queryId, String dataSetType, String countryCode, String dataProviderType, HttpServletResponse resp) throws IOException {
    ValueEnforcer.notEmpty(dataSetType, "dataSetType");
    ValueEnforcer.notEmpty(queryId, "queryId");

    LOGGER.debug("Processing query: [QueryId: " + queryId + ", dataSetType: " + dataSetType +
        ", countryCode: " + countryCode + ", dataProviderType: " + dataProviderType + "]");

    LOGGER.warn("Ingoring \"dataProviderType\":" + dataProviderType + " parameter for now");

    //currently only one type of query is supported
    switch (queryId) {
      case QUERY_DATASET_REQUEST: {
        SimpleDocumentTypeIdentifier doctypeId = new SimpleDocumentTypeIdentifier("", dataSetType);
        final ICommonsSet<IParticipantIdentifier> allParticipantIDs = ToopDirClient.getAllParticipantIDs(countryCode, doctypeId);

        //TODO we have participant IDs. Now convert them to REGREP + BregDcatAPasdfasdlka

        break;
      }

      default: {
        throw new IllegalArgumentException("Invalid queryId " + queryId);
      }
    }
  }
}
