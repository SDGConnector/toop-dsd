package eu.toop.dsd.service;


import java.io.StringWriter;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import eu.toop.dsd.client.DSDClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;

import eu.toop.dsd.client.DsdResponseReader;
import eu.toop.dsd.client.DsdResponseWriter;
import eu.toop.dsd.client.types.DoctypeParts;

public class DSDClientTest {

  @Ignore
  @Test
  public void testQuery() throws DatatypeConfigurationException {
    List<MatchType> matchTypes = DSDClient.queryDataset("http://dsd.dev.exchange.toop.eu", "REGISTERED_ORGANIZATION_TYPE", "SV");

    ResultListType rls = new ResultListType();
    rls.setVersion("1");
    rls.setQueryTerms("terms");
    GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
    rls.setCreationDt(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    matchTypes.forEach(matchType -> {
      rls.addMatch(matchType);
    });


    StringWriter writer = new StringWriter();
    PDSearchAPIWriter.resultListV1().write(rls, writer);
    System.out.println(writer);
  }
}


