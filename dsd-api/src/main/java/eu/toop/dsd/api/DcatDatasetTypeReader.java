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

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
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

public class DcatDatasetTypeReader {

  public static final String DATASET_SLOT_NAME = "Dataset";

  /**
   * Instantiates a new DcatDatasetTypeReader
   */
  /* hide the constructor */
  private DcatDatasetTypeReader() {
  }

  /**
   * Read match type list from a {@link Source} object
   *
   * @param aSource the {@link Source} object
   * @return the list of {@link DCatAPDatasetType} objects
   */
  @Nullable
  public static List<DCatAPDatasetType> parseDataset(@Nonnull final Source aSource) {
    QueryResponse queryResponse = RegRep4Reader.queryResponse(CCAGV.XSDS).read(aSource);
    if (queryResponse == null)
      return null;

    return convertQueryResponseToDCatAPDatasetTypeList(queryResponse);
  }

  private static List<DCatAPDatasetType> convertQueryResponseToDCatAPDatasetTypeList(QueryResponse queryResponse) {
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

    return convertElementsToDCatList(dcatElements);
  }

  /**
   * Convert the list of {@link Element} objects to a List of {@link DCatAPDatasetType} objects
   * @param dcatElements the list of {@link Element} objects. Not empty
   * @return the list of {@link DCatAPDatasetType} objects
   */
  private static List<DCatAPDatasetType> convertElementsToDCatList(List<Element> dcatElements) {
    List<DCatAPDatasetType> datasetTypes = new ArrayList<>(dcatElements.size());

    DatasetMarshaller dm = new DatasetMarshaller();
    dcatElements.forEach(element -> {
      DCatAPDatasetType dataset = dm.read(element);
      datasetTypes.add(dataset);
    });

    return datasetTypes;
  }
}
