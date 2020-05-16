package eu.toop.dsd.commons;

import com.helger.pd.searchapi.v1.MatchType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.AnyValueType;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * An intermedate class that writes {@link MatchType} objects.
 */
class MatchTypeListReader implements IReader<List<MatchType>> {

  /* hide the constructor */
  MatchTypeListReader(){ }

  @Override
  public List<MatchType> read(Reader reader) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(reader);
    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  @Override
  public List<MatchType> read(InputStream inputStream) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(inputStream);
    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  @Override
  public List<MatchType> fromString(String contents) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(contents);
    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  private List<MatchType> convertQueryResponseToMatchTypeList(QueryResponse queryResponse) {
    List<Element> dcatElements = new ArrayList<>();

    queryResponse.getRegistryObjectList().getRegistryObject().forEach(registryObjectType -> {
      registryObjectType.getSlot().forEach(slotType -> {
        //TODO: introduce a constant for "Dataset"
        if ("Dataset".equals(slotType.getName())){
          //this must be a dataset.
          Element dcatElement = (Element) ((AnyValueType)slotType.getSlotValue()).getAny();
          dcatElements.add(dcatElement);

        }
      });
    });

    return BregDCatHelper.convertDCatElementsToMatchTypes(dcatElements);
  }
}
