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


import eu.toop.dsd.api.DsdResponseWriter;
import eu.toop.dsd.api.ToopDirClient;
import eu.toop.dsd.config.DSDConfig;
import eu.toop.dsd.service.util.DSDQuery;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DSDTests {

  @Test
  public void basicXsltWithCountryCode() throws Exception {
    final String result = ToopDirClient.callSearchApiWithCountryCode(DSDConfig.getToopDirUrl(), "SV");
    System.out.println(result);
    String regRep = DsdResponseWriter.convertDIRToDSDWithCountryCode(result, "FINANCIAL_RECORD_TYPE", "SV");
    System.out.println(regRep);
  }


  @Test
  public void basicXsltWithDPType() throws Exception {
    final String result = ToopDirClient.callSearchApiWithIdentifierScheme(DSDConfig.getToopDirUrl(), "DataSubjectIdentifierScheme");
    System.out.println(result);
    String regRep = DsdResponseWriter.convertDIRToDSDWithDPType(result, "FINANCIAL_RECORD_TYPE", "DataSubjectIdentifierScheme");
    System.out.println(regRep);
  }

  public static void main(String[] args) throws Exception {

    Map<String, String[]> paramMap = new HashMap<>();

    paramMap.put(DSDQuery.PARAM_NAME_QUERY_ID, new String[]{DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_LOCATION.id});
    paramMap.put(DSDQuery.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE"});
    paramMap.put(DSDQuery.PARAM_NAME_COUNTRY_CODE, new String[]{"SV"});

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DSDQueryService.processRequest(paramMap, os);

    System.out.println(new String(os.toByteArray()));
    try (FileOutputStream xml = new FileOutputStream("sampleresponse.xml")) {
      xml.write(os.toByteArray());
    }
  }
}


