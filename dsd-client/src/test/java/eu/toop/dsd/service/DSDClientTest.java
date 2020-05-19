package eu.toop.dsd.service;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.Ignore;
import org.junit.Test;

import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;

import eu.toop.dsd.client.DSDClient;

public final class DSDClientTest {

  @Ignore
  @Test
  public void testQuery() throws DatatypeConfigurationException {
    final List<MatchType> matchTypes = new DSDClient("http://dsd.dev.exchange.toop.eu").queryDataset("REGISTERED_ORGANIZATION_TYPE",
                                                                                                     "SV");

    final ResultListType rls = new ResultListType();
    rls.setVersion("1");
    rls.setQueryTerms("terms");
    final GregorianCalendar c = (GregorianCalendar) Calendar.getInstance();
    rls.setCreationDt(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    matchTypes.forEach(matchType -> {
      rls.addMatch(matchType);
    });

    final StringWriter writer = new StringWriter();
    PDSearchAPIWriter.resultListV1().write(rls, writer);
    System.out.println(writer);
  }
}
