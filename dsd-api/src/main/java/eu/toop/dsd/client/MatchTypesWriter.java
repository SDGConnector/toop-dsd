package eu.toop.dsd.client;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import eu.toop.regrep.slot.SlotBuilder;
import org.w3c.dom.Document;

import com.helger.jaxb.IJAXBWriter;
import com.helger.pd.searchapi.v1.IDType;
import com.helger.pd.searchapi.v1.MatchType;

import eu.toop.edm.xml.IJAXBVersatileWriter;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.RegistryObjectListType;
import eu.toop.regrep.rim.RegistryObjectType;

/**
 * An intermediate class that writes {@link MatchType} objects.
 */
class MatchTypesWriter implements IJAXBVersatileWriter<QueryResponse> {
  private QueryResponse m_aQR;

  MatchTypesWriter(String s_DataSetType, List<MatchType> matchTypes) {
    m_aQR = prepareQueryResponse(s_DataSetType, matchTypes);
  }
  @Nonnull
  public QueryResponse getObjectToWrite ()
  {
    return m_aQR;
  }

  @Nonnull
  public IJAXBWriter <QueryResponse> getWriter ()
  {
    return RegRep4Writer.queryResponse(CCAGV.XSDS).setFormattedOutput(true);
  }

 

  /**
   * This is a tentative approach. We filter out match types as following:<br>
   * <pre>
   *   for each matchtype
   *     for each doctype of that matchtype
   *       remote the doctype if it does not contain datasetType
   *     if all doctypes were removed
   *        then remove the matchtype
   * </pre>
   *
   * @param s_datasetType Dataset type
   * @param matchTypes    Match types
   */
  public static void filterDirectoryResult(String s_datasetType, List<MatchType> matchTypes) {
    //filter
    final Iterator<MatchType> iterator = matchTypes.iterator();

    while (iterator.hasNext()) {
      MatchType matchType = iterator.next();
      final Iterator<IDType> iterator1 = matchType.getDocTypeID().iterator();
      while (iterator1.hasNext()) {
        IDType idType = iterator1.next();
        String concatenated = BregDCatHelper.flattenIdType(idType);

        // TODO: This is temporary, for now we are removing _ (underscore) and performing a case insensitive "contains" search

        //  ignore cases and underscores (CRIMINAL_RECORD = criminalRecord)
        if (!concatenated.replaceAll("_", "").toLowerCase()
            .contains(s_datasetType.replaceAll("_", "").toLowerCase())) {
          iterator1.remove();
        }
      }

      // if all doctypes have been removed then, eliminate this business card
      if (matchType.getDocTypeID().size() == 0) {
        iterator.remove();
      }
    }
  }

  /**
   * Convert the given <code>s_DataSetType</code> and <code>matchTypes</code> to a {@link QueryResponse} object
   * @param s_DataSetType
   * @param matchTypes
   * @return
   */
  public static QueryResponse prepareQueryResponse(String s_DataSetType, List<MatchType> matchTypes) {
    //Filter the matches that contain a part of the datasettype in their Doctypeids.
    filterDirectoryResult(s_DataSetType, matchTypes);
    List<Document> dcatDocuments = BregDCatHelper.convertMatchTypesToDCATDocuments(s_DataSetType, matchTypes);
    String sRequestID = UUID.randomUUID().toString();
    final QueryResponse aQResponse = RegRepHelper.createQueryResponse(ERegRepResponseStatus.SUCCESS, sRequestID);

    RegistryObjectListType registryObjectListType = new RegistryObjectListType();
    dcatDocuments.forEach(dcatDocument -> {
      RegistryObjectType registryObjectType = new RegistryObjectType();
      registryObjectType.setId(UUID.randomUUID().toString());
      registryObjectType.addSlot(new SlotBuilder().setName("Dataset")
          .setValue(dcatDocument.getDocumentElement())
          .build());
      registryObjectListType.addRegistryObject(registryObjectType);
    });

    aQResponse.setRegistryObjectList(registryObjectListType);
    aQResponse.setTotalResultCount(BigInteger.valueOf(dcatDocuments.size()));
    aQResponse.setStartIndex(BigInteger.ZERO);
    return aQResponse;
  }

}
