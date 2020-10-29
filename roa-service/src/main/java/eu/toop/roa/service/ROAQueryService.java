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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;


/**
 * This class is the main query processor for the ROA queries. It processes
 * the incoming queries and generates the required results with
 * respect to the TOOP ROA specification <a href="http://wiki.ds.unipi.gr/display/TOOP/.Registry+of+Authorities+v2.1">
 * http://wiki.ds.unipi.gr/display/TOOP/.Registry+of+Authorities+v2.1.1</a>.
 *
 * @author yerlibilgin
 */
public class ROAQueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ROAQueryService.class);
  public static final String QUERY_ROA_REQUEST = "urn:toop:roa:ebxml-regrep:queries:HasLegalMandateForProcedureAndRequirement";

  public static final String PARAM_NAME_QUERY_ID = "queryId";
  public static final String PARAM_NAME_DATA_CONSUMER_ID = "DataConsumerId";
  public static final String PARAM_NAME_PROCEDURE_ID = "procedureId";
  public static final String PARAM_NAME_REQUIREMENT_ID = "requirementId";


  /**
   * Query the underlying database for the provided parameters and
   * respond using the TOOP DSD RegRep response specification.
   *
   * @param parameterMap   the map that contains the parameters for the queries, may not be null
   * @return the result of the request
   * @throws IllegalArgumentException if the query parameters are invalid
   * @throws IllegalStateException    if a problem occurs
   */
  public static String processRequest(@Nonnull @Nonempty Map<String, String> parameterMap) {
    ValueEnforcer.notNull(parameterMap, "parameterMap");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");

    //try to get the queryId from the map
    String queryId = parameterMap.get(PARAM_NAME_QUERY_ID);
    ValueEnforcer.notEmpty(queryId, "queryId");

    LOGGER.debug("Processing query: [QueryId: " + queryId + "");

    //currently only one type of query is supported
    switch (queryId) {
      case QUERY_ROA_REQUEST: {
        try {
          return processDataSetRequest(parameterMap);
        } catch (IOException e) {
          throw new IllegalStateException(e.getMessage(), e);
        }
      }

      default: {
        throw new IllegalArgumentException("Invalid queryId " + queryId);
      }
    }
  }

  /**
   * Processes the incoming parameter map as a dataset request parameter map and performs a data consumer request.
   *
   * @param parameterMap   the map that contains the parameters to the query. May not be null
   * @return the result of the request
   * @throws IOException if an io problem occurs.
   */
  public static String processDataSetRequest(@Nonnull @Nonempty Map<String, String> parameterMap) throws IOException {
    ValueEnforcer.notNull(parameterMap, "parameterMap");

    if (parameterMap.isEmpty())
      throw new IllegalArgumentException("parameterMap cannot be empty");

    String dataConsumerId = parameterMap.get(PARAM_NAME_DATA_CONSUMER_ID);
    ValueEnforcer.notEmpty(dataConsumerId, "dataConsumerId");

    String procedureId = parameterMap.get(PARAM_NAME_PROCEDURE_ID);
    ValueEnforcer.notEmpty(procedureId, "procedureId");

    String requirementId = parameterMap.get(PARAM_NAME_REQUIREMENT_ID);
    ValueEnforcer.notEmpty(requirementId, "requirementId");

    LOGGER.debug("Processing data consumer request \n"
        + "    data consumer id: " + dataConsumerId + "\n"
        + "    procedureId: " + procedureId + "\n"
        + "    requirementId: " + requirementId);


    return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<query:QueryResponse xmlns=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0\"\n" +
        "                     xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0\"\n" +
        "                     xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0\"\n" +
        "                     xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0\"\n" +
        "                     xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0\"\n" +
        "                     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "                     xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0\"\n" +
        "                     totalResultCount=\"1\"\n" +
        "                     startIndex=\"0\"\n" +
        "                     status=\"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success\">\n" +
        "  <!-- depending on the count of datasets returned, the totalResultCount attribute should\n" +
        "  reflect the number of the datasets returned -->\n" +
        "  <rim:RegistryObjectList>\n" +
        " \n" +
        "    <!-- One registry object per dataset -->\n" +
        "    <rim:RegistryObject id=\"RE238912378\">\n" +
        "      <rim:Slot name=\"CompetentAuthority\">\n" +
        "        <rim:SlotValue xsi:type=\"rim:AnyValueType\">\n" +
        "           <cagv:Agent xmlns:cagv=\"https://semic.org/sa/cv/cagv/agent-2.0.0#\"\n" +
        "            xmlns:cbc=\"https://semic.org/sa/cv/common/cbc-2.0.0#\"\n" +
        "            xmlns:locn=\"http://www.w3.org/ns/locn#\">\n" +
        "            <cbc:id schemeID=\"VAT\">RE238912378</cbc:id>\n" +
        "            <cbc:name>aCompanyName</cbc:name>\n" +
        "            <cagv:location>\n" +
        "                <locn:address>\n" +
        "                    <locn:fullAddress>Prince Street 15</locn:fullAddress>\n" +
        "                    <locn:thoroughfare>Prince Street</locn:thoroughfare>\n" +
        "                    <locn:locatorDesignator>15</locn:locatorDesignator>\n" +
        "                    <locn:postName>LiverPool</locn:postName>\n" +
        "                    <locn:adminUnitLevel1>GB</locn:adminUnitLevel1>\n" +
        "                    <locn:postCode>15115</locn:postCode>\n" +
        "                </locn:address>\n" +
        "            </cagv:location>\n" +
        "        </cagv:Agent>   \n" +
        "        </rim:SlotValue>\n" +
        "      </rim:Slot>\n" +
        " \n" +
        "      <rim:Slot name=\"Procedure\">\n" +
        "        <rim:SlotValue xsi:type=\"StringValueType\">\n" +
        "          <rim:Value>GBM_PROCEDURE</rim:Value>\n" +
        "        </rim:SlotValue>\n" +
        "      </rim:Slot>\n" +
        " \n" +
        "      <rim:Slot name=\"HasLegalMandate\">\n" +
        "        <rim:SlotValue xsi:type=\"BooleanValueType\">\n" +
        "          <rim:Value>true</rim:Value>\n" +
        "        </rim:SlotValue>\n" +
        "      </rim:Slot>\n" +
        " \n" +
        "    </rim:RegistryObject>\n" +
        "  </rim:RegistryObjectList>\n" +
        "</query:QueryResponse>");
  }
}
