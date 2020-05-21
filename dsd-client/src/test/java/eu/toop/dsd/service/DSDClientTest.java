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

import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import org.junit.Ignore;
import org.junit.Test;

import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;

import eu.toop.dsd.client.DSDClient;

/**
 * A test class that tests the DSDClient
 *
 * @author yerlibilgin
 */
public final class DSDClientTest {

  /**
   * Test query
   *
   * @throws DatatypeConfigurationException the datatype configuration exception
   */
  @Ignore
  @Test
  public void testQuery() throws DatatypeConfigurationException {
    final List<DCatAPDatasetType> matchTypes = new DSDClient("http://dsd.dev.exchange.toop.eu/").queryDataset("REGISTERED_ORGANIZATION_TYPE",
        "SV");

    final DatasetMarshaller datasetMarshaller = new DatasetMarshaller();
    datasetMarshaller.setFormattedOutput(true);

    matchTypes.forEach(dataset -> {
      System.out.println(datasetMarshaller.getAsString(dataset));
    });
  }


  /**
   * Test query as match types.
   *
   * @throws DatatypeConfigurationException the datatype configuration exception
   */
  @Ignore
  @Test
  public void testQuerMatchTypes() throws DatatypeConfigurationException {
    final List<MatchType> matchTypes = new DSDClient("http://dsd.dev.exchange.toop.eu/").queryDatasetAsMatchTypes("REGISTERED_ORGANIZATION_TYPE",
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
    PDSearchAPIWriter.resultListV1().setFormattedOutput(true).write(rls, writer);
    System.out.println(writer);
  }
}
