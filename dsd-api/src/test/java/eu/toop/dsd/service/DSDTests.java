package eu.toop.dsd.service;


import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.Assert;
import org.junit.Test;

import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;

import eu.toop.dsd.client.DsdResponseReader;
import eu.toop.dsd.client.DsdResponseWriter;
import eu.toop.dsd.client.types.DoctypeParts;

public class DSDTests {

  @Test
  public void testConvertMatchTypes() {
    final ResultListType read = PDSearchAPIReader.resultListV1().read(DSDTests.class.getResourceAsStream("/directory-results.xml"));
    final List<MatchType> match = read.getMatch();

    String resultXml = DsdResponseWriter.matchTypesWriter("mydatasettype", match).getAsString();
    System.out.println(resultXml);
  }

  @Test
  public void testConvertSingleMatchType() {
    final ResultListType read = PDSearchAPIReader.resultListV1().read(DSDTests.class.getResourceAsStream("/directory-result-single.xml"));
    final List<MatchType> match = read.getMatch();
    final String s_dataSetType = "REGISTERED_ORGANIZATION";

    String resultXml = DsdResponseWriter.matchTypesWriter(s_dataSetType, match).getAsString();
    System.out.println(resultXml);
  }


  @Test
  public void writeRead() throws DatatypeConfigurationException {
    final ResultListType read = PDSearchAPIReader.resultListV1().read(DSDTests.class.getResourceAsStream("/directory-result-single.xml"));
    final List<MatchType> match = read.getMatch();
    final String s_dataSetType = "S";

    String resultXml = DsdResponseWriter.matchTypesWriter(s_dataSetType, match).getAsString();
    System.out.println(resultXml);

    List<MatchType> matchTypeList = DsdResponseReader.matchTypeListReader().read(resultXml);


    ResultListType rls = new ResultListType();
    rls.setVersion("1");
    rls.setQueryTerms("terms");
    GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
    rls.setCreationDt(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    matchTypeList.forEach(matchType -> {
      rls.addMatch(matchType);
    });


    System.out.println(PDSearchAPIWriter.resultListV1().setFormattedOutput(true).getAsString(rls));
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
}


