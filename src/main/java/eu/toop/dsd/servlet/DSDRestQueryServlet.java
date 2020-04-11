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
package eu.toop.dsd.servlet;

import com.helger.commons.ValueEnforcer;
import eu.toop.dsd.service.DSDQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The HTTP servlet for the REST query. One servlet could be used
 * for handling both the get and post requests, but it the specification
 * {http://docs.oasis-open.org/regrep/regrep-core/v4.0/os/regrep-core-rs-v4.0-os.html#__RefHeading__32747_422331532}
 * suggests using "/rest/search" for the rest based query, which would
 * be meaningless for handling a Post request that is not REST.
 *
 * @author Muhammet Yildiz
 */
@WebServlet("/rest/search")
public class DSDRestQueryServlet extends HttpServlet {

  public static final String QUERY_DATASET_REQUEST = "urn:toop:dsd:ebxml-regrep:queries:DataSetRequest";

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDRestQueryServlet.class);
  public static final String PARAM_NAME_DATA_SET_TYPE = "DataSetType";
  public static final String PARAM_NAME_QUERY_ID = "queryId";
  private static final String PARAM_NAME_COUNTRY_CODE = "CountryCode";
  private static final String PARAM_NAME_DATA_PROVIDER_TYPE = "DataProviderType";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOGGER.debug("DSD query  with " + req.getQueryString());

    //parse the query parameters
    final String queryId = req.getParameter(PARAM_NAME_QUERY_ID);
    ValueEnforcer.notEmpty(queryId, PARAM_NAME_QUERY_ID);

    //currently only one type of query is supported
    switch (queryId) {
      case QUERY_DATASET_REQUEST: {
        //the query id is valid. Now check for the mandatory parameter
        //DataSetType


        final String dataSetType = req.getParameter(PARAM_NAME_DATA_SET_TYPE);
        ValueEnforcer.notEmpty(dataSetType, PARAM_NAME_DATA_SET_TYPE);

        //the rest is optional. Ignore the unknown parameters

        DSDQueryService.processRequest(queryId, dataSetType,
            req.getParameter(PARAM_NAME_COUNTRY_CODE),
            req.getParameter(PARAM_NAME_DATA_PROVIDER_TYPE), resp);

        break;
      }

      default: {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // TODO create an error processor to respond with
        //  the specified RegRep error
        resp.getOutputStream().println("Invalid queryId");
      }
    }
  }
}
