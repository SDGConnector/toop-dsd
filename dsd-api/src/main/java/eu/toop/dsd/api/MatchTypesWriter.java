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
package eu.toop.dsd.api;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import eu.toop.regrep.slot.SlotBuilder;
import org.w3c.dom.Document;

import com.helger.jaxb.IJAXBWriter;
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
  public QueryResponse getObjectToWrite() {
    return m_aQR;
  }

  @Nonnull
  public IJAXBWriter<QueryResponse> getWriter() {
    return RegRep4Writer.queryResponse(CCAGV.XSDS).setFormattedOutput(true);
  }

  /**
   * Convert the given <code>s_DataSetType</code> and <code>matchTypes</code> to a {@link QueryResponse} object
   * @param s_DataSetType to be used in response creation
   * @param matchTypes the match types for each a dataset will be created
   * @return the parsed QueryResponse
   */
  public static QueryResponse prepareQueryResponse(String s_DataSetType, List<MatchType> matchTypes) {
    //Filter the matches that contain a part of the datasettype in their Doctypeids.
    DSDTypesManipulator.filterDirectoryResults(s_DataSetType, null, matchTypes);
    List<Document> dcatDocuments = DSDTypesManipulator.convertMatchTypesToDCATDocuments(s_DataSetType, matchTypes);
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
