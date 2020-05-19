package eu.toop.dsd.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.Source;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.helger.pd.searchapi.v1.MatchType;

import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.AnyValueType;

/**
 * An intermedate class that writes {@link MatchType} objects.
 */
class MatchTypeListReader implements IJAXBVersatileReader<List<MatchType>> {

  /* hide the constructor */
  MatchTypeListReader(){ }

  @Nullable
  public List<MatchType> read (@Nonnull final Source aSource)
  {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aSource);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  @Nullable
  public List<MatchType> read (@Nonnull final Node aNode)
  {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aNode);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  private static List<MatchType> convertQueryResponseToMatchTypeList(QueryResponse queryResponse) {
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
