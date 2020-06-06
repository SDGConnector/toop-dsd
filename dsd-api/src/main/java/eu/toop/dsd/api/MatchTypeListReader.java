/**
 * Copyright (C) 2018-2020 toop.eu
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
 */
package eu.toop.dsd.api;

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

  public static final String DATASET_SLOT_NAME = "Dataset";

  /**
   * Instantiates a new Match type list reader.
   */
  /* hide the constructor */
  MatchTypeListReader() {
  }

  @Nullable
  public List<MatchType> read(@Nonnull final Source aSource) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aSource);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  /**
   * Read match type list from a {@link Node} object
   *
   * @param aNode the {@link Node} object
   * @return the list of match types
   */
  @Nullable
  public List<MatchType> read(@Nonnull final Node aNode) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aNode);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToMatchTypeList(queryResponse);
  }

  private static List<MatchType> convertQueryResponseToMatchTypeList(QueryResponse queryResponse) {
    List<Element> dcatElements = new ArrayList<>();

    queryResponse.getRegistryObjectList().getRegistryObject().forEach(registryObjectType -> {
      registryObjectType.getSlot().forEach(slotType -> {
        if (DATASET_SLOT_NAME.equals(slotType.getName())) {
          //this must be a dataset.
          Element dcatElement = (Element) ((AnyValueType) slotType.getSlotValue()).getAny();
          dcatElements.add(dcatElement);

        }
      });
    });

    return BregDCatHelper.convertDCatElementsToMatchTypes(BregDCatHelper.convertElementsToDCatList(dcatElements));
  }
}
