/**
 * Copyright (C) 2018-2021 toop.eu. All rights reserved.
 *
 * This project is dual licensed under Apache License, Version 2.0
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
package eu.toop.dsd.api.types;

import org.junit.Test;

public class DoctypePartsTest {
    @Test
    public void parse() {

        String[] docTypes = new String[]{
                "toop-doctypeid-qns::RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.1",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization-list::1.40",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.request.registeredorganization-list::1.40",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.request.registeredorganization::1.40",
                "toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10",
                "toop-doctypeid-qns::FinancialRatioDocument::FINANCIAL_RECORD_TYPE::UNSTRUCTURED::toop-edm:v2.1"
        };

        for(String str : docTypes) {
            DoctypeParts doctypeParts = DoctypeParts.parse(str);
            System.out.println(doctypeParts);
        }
    }
}
