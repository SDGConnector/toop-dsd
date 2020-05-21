package eu.toop.dsd.client;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.AnyValueType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

public class DcatDatasetTypeReader implements IJAXBVersatileReader<List<DCatAPDatasetType>> {

  /**
   * Instantiates a new DcatDatasetTypeReader
   */
  /* hide the constructor */
  DcatDatasetTypeReader() {
  }

  /**
   * Read match type list from a {@link Source} object
   *
   * @param aSource the {@link Source} object
   * @return the list of {@link DCatAPDatasetType} objects
   */
  @Nullable
  public List<DCatAPDatasetType> read(@Nonnull final Source aSource) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aSource);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToDCatAPDatasetTypeList(queryResponse);
  }

  /**
   * Read match type list from a {@link Node} object
   *
   * @param aNode the {@link Node} object
   * @return the list of {@link DCatAPDatasetType} objects
   */
  @Nullable
  public List<DCatAPDatasetType> read(@Nonnull final Node aNode) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aNode);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToDCatAPDatasetTypeList(queryResponse);
  }

  private static List<DCatAPDatasetType> convertQueryResponseToDCatAPDatasetTypeList(QueryResponse queryResponse) {
    List<Element> dcatElements = new ArrayList<>();

    queryResponse.getRegistryObjectList().getRegistryObject().forEach(registryObjectType -> {
      registryObjectType.getSlot().forEach(slotType -> {
        //TODO: introduce a constant for "Dataset"
        if ("Dataset".equals(slotType.getName())) {
          //this must be a dataset.
          Element dcatElement = (Element) ((AnyValueType) slotType.getSlotValue()).getAny();
          dcatElements.add(dcatElement);

        }
      });
    });

    return BregDCatHelper.convertElementsToDCatList(dcatElements);
  }
}
