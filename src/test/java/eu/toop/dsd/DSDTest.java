package eu.toop.dsd;


import eu.toop.dsd.service.DSDQueryService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DSDTest{
  public static void main(String[] args) throws IOException {

    Map<String, String[]> paramMap = new HashMap<>();

    paramMap.put(DSDQueryService.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE"});
    paramMap.put(DSDQueryService.PARAM_NAME_COUNTRY_CODE, new String[]{"GQ"});

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DSDQueryService.processDataSetRequest(paramMap, os);

    System.out.println(new String(os.toByteArray()));
  }
}


