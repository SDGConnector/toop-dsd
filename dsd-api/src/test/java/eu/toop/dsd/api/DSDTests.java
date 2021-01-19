/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2020-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.dsd.api;


import org.junit.Assert;
import org.junit.Test;

import eu.toop.dsd.api.types.DoctypeParts;

/**
 * The type Dsd tests.
 *
 * @author yerlibilgin
 */
public class DSDTests {
  /**
   * Test doctype parts.
   */
  @Test
  public void testDoctypeParts() {
    DoctypeParts parts = DoctypeParts.parse(
        "toop-doctypeid-qns::RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.1");
    Assert.assertNotNull(parts);

    Assert.assertEquals("RegisteredOrganization", parts.getDataSetIdentifier());
    Assert.assertEquals("REGISTERED_ORGANIZATION_TYPE", parts.getDatasetType());
    Assert.assertEquals("CONCEPT", parts.getDistributionFormat());
    Assert.assertEquals("CCCEV", parts.getDistributionConformsTo());
    Assert.assertEquals("toop-edm:v2.1", parts.getConformsTo());
  }

  /**
   * Test doctype parts old doc type.
   */
  @Test
  public void testDoctypePartsOldDocType() {
    DoctypeParts parts = DoctypeParts.parse(
        "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40");
    Assert.assertNotNull(parts);

    Assert.assertEquals("urn:eu:toop:ns:dataexchange-1p40", parts.getDatasetType());
    Assert.assertEquals("Request", parts.getDistributionFormat());
    Assert.assertEquals("urn:eu.toop.request.registeredorganization", parts.getDistributionConformsTo());
    Assert.assertEquals("1.40", parts.getConformsTo());
  }

  @Test
  public void basicXsltWithCountryCode() throws Exception {
    final String result = ToopDirClient.callSearchApiWithCountryCode(ToopDirClientTest.TOOP_DIR_URL, "SV");
    System.out.println(result);
    String regRep = DsdDataConverter.convertDIRToDSDWithCountryCode(result, "FINANCIAL_RECORD_TYPE", "SV");
    System.out.println(regRep);
  }


  @Test
  public void basicXsltWithDPType() throws Exception {
    final String result = ToopDirClient.callSearchApiForDpType(ToopDirClientTest.TOOP_DIR_URL, "abc");
    System.out.println(result);
    String regRep = DsdDataConverter.convertDIRToDSDWithDPType(result, "REGISTERED_ORGANIZATION_TYPE", "abc");
    System.out.println(regRep);
  }
}


