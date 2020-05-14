package eu.toop.dsd.service;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
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
    PDSearchAPIWriter<ResultListType> p = PDSearchAPIWriter.resultListV1();
    p.setFormattedOutput(true);

    try (StringWriter writer = new StringWriter()) {
      p.write(resultListType, writer);
      System.out.println("Writer: " + writer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testDoctypeParts() {
    DoctypeParts parts = DoctypeParts.parse(
        "toop-doctypeid-qns::RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.0");
    Assert.assertNotNull(parts);

    Assert.assertEquals("toop-doctypeid-qns", parts.getScheme());
    Assert.assertEquals("RegisteredOrganization", parts.getDataSetIdentifier());
    Assert.assertEquals("REGISTERED_ORGANIZATION_TYPE", parts.getDatasetType());
    Assert.assertEquals("CONCEPT", parts.getDistributionFormat());
    Assert.assertEquals("CCCEV", parts.getDistributionConformsTo());
    Assert.assertEquals("toop-edm:v2.0", parts.getConformsTo());
  }


  @Test
  public void testDoctypePartsNoSchem() {
    DoctypeParts parts = DoctypeParts.parse(
        "RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.0");
    Assert.assertNotNull(parts);

    Assert.assertEquals(null, parts.getScheme());
    Assert.assertEquals("RegisteredOrganization", parts.getDataSetIdentifier());
    Assert.assertEquals("REGISTERED_ORGANIZATION_TYPE", parts.getDatasetType());
    Assert.assertEquals("CONCEPT", parts.getDistributionFormat());
    Assert.assertEquals("CCCEV", parts.getDistributionConformsTo());
    Assert.assertEquals("toop-edm:v2.0", parts.getConformsTo());
  }


  @Test
  public void testDoctypePartsOldDocType() {
    DoctypeParts parts = DoctypeParts.parse(
        "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40");
    Assert.assertNotNull(parts);

    Assert.assertEquals(null, parts.getScheme());
    Assert.assertEquals("toop-doctypeid-qns", parts.getDataSetIdentifier());
    Assert.assertEquals("urn:eu:toop:ns:dataexchange-1p40", parts.getDatasetType());
    Assert.assertEquals("Request", parts.getDistributionFormat());
    Assert.assertEquals("urn:eu.toop.request.registeredorganization", parts.getDistributionConformsTo());
    Assert.assertEquals("1.40", parts.getConformsTo());
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


