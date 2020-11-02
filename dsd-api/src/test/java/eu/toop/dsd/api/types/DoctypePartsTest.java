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
