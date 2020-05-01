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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.dsd.service.DSDQueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HTTP servlet for the REST query. One servlet could be used
 * for handling both the get and post requests, but it the specification
 * {http://docs.oasis-open.org/regrep/regrep-core/v4.0/os/regrep-core-rs-v4.0-os.html#__RefHeading__32747_422331532}
 * suggests using "/rest/search" for the rest based query, which would
 * be meaningless for handling a Post request that is not REST.
 *
 * @author @yerlibilgin
 */
@WebServlet("/rest/search")
public class DSDRestQueryServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDRestQueryServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOGGER.debug("DSD query  with " + req.getQueryString());

    try {
      Map<String, String[]> parameterMap = req.getParameterMap();
      DSDQueryService.processRequest(parameterMap, resp.getOutputStream());
    } catch (IllegalArgumentException ex) {
      //convert this to bad request
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
      //convert this to internal server error
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      final String message = ex.getMessage();
      resp.getOutputStream().println(message != null ? message : "UNKNOWN ERROR");
    }
  }
}
