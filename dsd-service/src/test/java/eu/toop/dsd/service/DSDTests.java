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


