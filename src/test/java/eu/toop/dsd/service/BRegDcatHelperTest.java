package eu.toop.dsd.service;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import com.helger.pd.searchapi.v1.MatchType;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.ResultListType;

public class BRegDcatHelperTest {

  @Test
  public void testConvertMatchTypes() {
    final ResultListType read = PDSearchAPIReader.resultListV1().read(BRegDcatHelperTest.class.getResourceAsStream("/directory-results.xml"));
    final List<MatchType> match = read.getMatch();
    final List<Document> documents = BregDCatHelper.convertBusinessCardsToDCat("mydatasettype", match);
    String resultXml = DSDRegRep.createQueryResponse(UUID.randomUUID().toString(), documents);
    System.out.println(resultXml);
  }

  @Test
  public void testConvertSingleMatchType() {
    final ResultListType read = PDSearchAPIReader.resultListV1().read(BRegDcatHelperTest.class.getResourceAsStream("/directory-result-single.xml"));
    final List<MatchType> match = read.getMatch();
    final String s_dataSetType = "REGISTERED_ORGANIZATION";
    DSDQueryService.filterDirectoryResult(s_dataSetType, match);
    final List<Document> documents = BregDCatHelper.convertBusinessCardsToDCat(s_dataSetType, match);
    String resultXml = DSDRegRep.createQueryResponse(UUID.randomUUID().toString(), documents);
    System.out.println(resultXml);
  }


  @Ignore
  @Test
  public void saveSearches() throws IOException {
    final ResultListType resultListType = ToopDirClient.performSearchResultsLists(null, null);
    PDSearchAPIWriter p = PDSearchAPIWriter.resultListV1();
    p.setFormattedOutput(true);

    try (StringWriter writer = new StringWriter()) {
      p.write(resultListType, writer);
      System.out.println("Writer: " + writer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testDoctypeEdm20Compliant(){
    //Matcher matcher = BregDCatHelper.eDM20ComplianceMatcher("RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.0");
    Matcher matcher = BregDCatHelper.eDM20ComplianceMatcher("RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT::toop-edm");
    System.out.println(matcher.matches());
  }

  public static void main(String[] args) throws IOException {

    Map<String, String[]> paramMap = new HashMap<>();

    //paramMap.put(DSDQueryService.PARAM_NAME_DATA_SET_TYPE, new String[]{"REGISTERED_ORGANIZATION_TYPE"});
    //paramMap.put(DSDQueryService.PARAM_NAME_COUNTRY_CODE, new String[]{"GQ"});

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DSDQueryService.processDataSetRequest(paramMap, os);

    System.out.println(new String(os.toByteArray()));
    try (FileOutputStream xml = new FileOutputStream("sampleresponse.xml")) {
      xml.write(os.toByteArray());
    }
  }
}


