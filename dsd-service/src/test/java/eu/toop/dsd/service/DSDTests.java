/**
 * Copyright (C) 2018-2020 toop.eu
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
 */
package eu.toop.dsd.service;


import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.ResultListType;
import eu.toop.dsd.config.DSDConfig;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class DSDTests {

  @Ignore
  @Test
  public void saveSearches() throws IOException {
    final ResultListType resultListType = ToopDirClient.performSearchResultsLists(DSDConfig.getToopDirUrl(), null, null);
    PDSearchAPIWriter<ResultListType> p = PDSearchAPIWriter.resultListV1();
    p.setFormattedOutput(true);

    try (StringWriter writer = new StringWriter()) {
      p.write(resultListType, writer);
      System.out.println("Writer: " + writer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  public static void main(String[] args) throws IOException {

    Map<String, String[]> paramMap = new HashMap<>();

    paramMap.put(DSDQueryService.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE"});
    paramMap.put(DSDQueryService.PARAM_NAME_COUNTRY_CODE, new String[]{"SV"});

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DSDQueryService.processDataSetRequest(paramMap, os);

    System.out.println(new String(os.toByteArray()));
    try (FileOutputStream xml = new FileOutputStream("sampleresponse.xml")) {
      xml.write(os.toByteArray());
    }
  }
}


