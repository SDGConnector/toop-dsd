package eu.toop.dsd.service;

import eu.toop.edm.regrep.RegRepHelper;
import eu.toop.edm.regrep.SlotBuilder;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.math.BigInteger;
import java.util.List;

public class DSDRegRep {
  public static String createQueryResponse(String sReqesutID, List<Document> dcatDocuments) {

    final QueryResponse aQResponse = RegRepHelper.createQueryResponse(sReqesutID);

    dcatDocuments.forEach(dcatDocument -> {
      aQResponse.addSlot(new SlotBuilder().setName("Dataset").setValue(
          dcatDocument.getDocumentElement()
      ).build());
    });

    aQResponse.setTotalResultCount(BigInteger.valueOf(dcatDocuments.size()));
    aQResponse.setStartIndex(BigInteger.ZERO);
    return RegRep4Writer.queryResponse().setFormattedOutput (true).getAsString(aQResponse);
  }
}
