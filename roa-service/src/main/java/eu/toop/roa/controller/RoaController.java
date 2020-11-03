/*
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
package eu.toop.roa.controller;

import eu.toop.roa.model.Address;
import eu.toop.roa.model.Agent;
import eu.toop.roa.service.ROAQueryService;
import io.jooby.Context;
import io.jooby.StatusCode;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Path("/rest")
public class RoaController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RoaController.class);

  @Inject
  public RoaController() {
  }

  @GET("/roaList")
  public List<Agent> getAgents(Context context) {
    context.setResponseType("text/json");
    return Arrays.asList(
        new Agent("RE238912371", "VAT", "aCompanyName1", new Address("fullAddress1", "street1", "building1", "town1", "PC1", "TR1")),
        new Agent("RE238912372", "VAT", "aCompanyName2", new Address("fullAddress2", "street2", "building2", "town2", "PC2", "TR2")),
        new Agent("RE238912373", "VAT", "aCompanyName3", new Address("fullAddress3", "street3", "building3", "town3", "PC3", "TR3")),
        new Agent("RE238912374", "VAT", "aCompanyName4", new Address("fullAddress4", "street4", "building4", "town4", "PC4", "TR4")),
        new Agent("RE238912375", "VAT", "aCompanyName5", new Address("fullAddress5", "street5", "building5", "town5", "PC5", "TR5"))
    );
  }

  @GET("/search")
  public String search(Context ctx) {
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
