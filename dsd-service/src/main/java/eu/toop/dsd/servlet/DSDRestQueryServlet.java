/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2020-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.dsd.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.dsd.api.DsdDataConverter;
import eu.toop.dsd.api.ToopDirClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.dsd.service.DSDQueryService;

/**
 * The HTTP servlet for the REST query. One servlet could be used
 * for handling both the get and post requests, but it the specification
 * {http://docs.oasis-open.org/regrep/regrep-core/v4.0/os/regrep-core-rs-v4.0-os.html#__RefHeading__32747_422331532}
 * suggests using "/rest/search" for the rest based query, which would
 * be meaningless for handling a Post request that is not REST.
 *
 * @author yerlibilgin
 */
@WebServlet(value = "/rest/search", loadOnStartup = 1)
public class DSDRestQueryServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDRestQueryServlet.class);

  public DSDRestQueryServlet() {
    LOGGER.debug("Creating DSDRestQueryServlet");

    //Preapre transfomation for further uses
    DsdDataConverter.prepare();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LOGGER.debug("DSD query  with " + req.getQueryString());

    try {
      Map<String, String[]> parameterMap = req.getParameterMap();
      DSDQueryService.processRequest(parameterMap, resp.getOutputStream());
      resp.setContentType("application/xml");
    } catch (Exception ex) {
      resp.setContentType("text/plain");
      LOGGER.error(ex.getMessage(), ex);

      if (ex instanceof IllegalStateException) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }

      final String message = ex.getMessage();
      resp.getOutputStream().println(message != null ? message : "UNKNOWN ERROR");
    }
  }


}
