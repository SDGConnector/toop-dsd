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
package eu.toop.roa.main;

import eu.toop.roa.config.ROAConfig;
import eu.toop.roa.model.Agent;
import eu.toop.roa.service.ROAQueryService;
import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.json.JacksonModule;
import io.jooby.thymeleaf.ThymeleafModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The HTTP servlet for the REST query. One servlet could be used
 * for handling both the get and post requests, but it the specification
 * {http://docs.oasis-open.org/regrep/regrep-core/v4.0/os/regrep-core-rs-v4.0-os.html#__RefHeading__32747_422331532}
 * suggests using "/rest/search" for the rest based query, which would
 * be meaningless for handling a Post request that is not REST.
 *
 * @author yerlibilgin
 */
public class ROAMain extends Jooby {

  private static final Logger LOGGER = LoggerFactory.getLogger(ROAMain.class);

  {
    install(new ThymeleafModule("/views/templates"));
    install(new JacksonModule());
    assets("/static/*", "/views/static");

    Map<String, Object> model = new HashMap<>();
    model.put("roaVersion", ROAConfig.getRoaVersion());
    model.put("buildDate", ROAConfig.getBuildDate());
    get("/", ctx -> new ModelAndView("index.html", model));
    get("/roaList", ctx -> new ModelAndView("roalist.html", model));
    get("/rest/search", ctx -> doGet(ctx));
    mvc(new RoaController());
  }

  public static void main(String[] args) {
    LOGGER.info("Running TOOP Registry of Authorities V" + ROAConfig.getRoaVersion());
    runApp(args, ROAMain::new);
  }

  private String doGet(Context ctx) {
    LOGGER.debug("ROA query  with " + ctx.queryString());

    try {
      Map<String, String> parameterMap = ctx.queryMap();
      ctx.setResponseType("application/xml");
      return ROAQueryService.processRequest(parameterMap);
    } catch (Exception ex) {
      ctx.setResponseType("text/plain");
      LOGGER.error(ex.getMessage(), ex);

      if (ex instanceof IllegalStateException) {
        ctx.setResponseCode(StatusCode.BAD_REQUEST);
      } else {
        ctx.setResponseCode(StatusCode.SERVER_ERROR);
      }

      final String message = ex.getMessage();
      return message;
    }
  }
}
