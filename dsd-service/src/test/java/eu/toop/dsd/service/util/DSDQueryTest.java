package eu.toop.dsd.service.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DSDQueryTest {
  private static DSDQuery QUERY_BY_DATASETTYPE_AND_DPTYPE;
  private static DSDQuery QUERY_BY_DATASETTYPE_AND_LOCATION;

  @BeforeClass
  public static void beforeClass() {
    Map<String, String[]> parameterMap = new HashMap<>();
    parameterMap.put(DSDQuery.PARAM_NAME_QUERY_ID, new String[]{DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_DPTYPE.id});
    parameterMap.put(DSDQuery.PARAM_NAME_DATA_PROVIDER_TYPE, new String[]{"9999:elonia"});
    parameterMap.put(DSDQuery.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE1"});

    QUERY_BY_DATASETTYPE_AND_DPTYPE = DSDQuery.resolve(parameterMap);


    parameterMap.clear();
    parameterMap.put(DSDQuery.PARAM_NAME_QUERY_ID, new String[]{DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_LOCATION.id});
    parameterMap.put(DSDQuery.PARAM_NAME_COUNTRY_CODE, new String[]{"SW"});
    parameterMap.put(DSDQuery.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE2"});

    QUERY_BY_DATASETTYPE_AND_LOCATION = DSDQuery.resolve(parameterMap);
  }

  @Test
  public void getParameterValue() {
    assert "SW".equals(QUERY_BY_DATASETTYPE_AND_LOCATION.getParameterValue(DSDQuery.PARAM_NAME_COUNTRY_CODE));
    assert "REGISTERED_ORGANIZATION_TYPE2".equals(QUERY_BY_DATASETTYPE_AND_LOCATION.getParameterValue(DSDQuery.PARAM_NAME_DATA_SET_TYPE));
    assert DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_LOCATION.id.equals(QUERY_BY_DATASETTYPE_AND_LOCATION.getParameterValue(DSDQuery.PARAM_NAME_QUERY_ID));


    assert "9999:elonia".equals(QUERY_BY_DATASETTYPE_AND_DPTYPE.getParameterValue(DSDQuery.PARAM_NAME_DATA_PROVIDER_TYPE));
    assert "REGISTERED_ORGANIZATION_TYPE1".equals(QUERY_BY_DATASETTYPE_AND_DPTYPE.getParameterValue(DSDQuery.PARAM_NAME_DATA_SET_TYPE));
    assert DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_DPTYPE.id.equals(QUERY_BY_DATASETTYPE_AND_DPTYPE.getParameterValue(DSDQuery.PARAM_NAME_QUERY_ID));
  }

  @Test
  public void getQueryId() {
    assert DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_LOCATION == QUERY_BY_DATASETTYPE_AND_LOCATION.getQueryId();
    assert DSDQuery.DSDQueryID.QUERY_BY_DATASETTYPE_AND_DPTYPE == QUERY_BY_DATASETTYPE_AND_DPTYPE.getQueryId();
  }
}
