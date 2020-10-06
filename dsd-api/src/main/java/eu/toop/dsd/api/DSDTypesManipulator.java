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


import com.helger.pd.searchapi.v1.EntityType;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.NameType;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.w3.locn.AddressType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for manipulating various types that DSD uses.
 *
 * @author yerlibilgin
 */
public class DSDTypesManipulator {
  /**
   * Convert the list of {@link Element} objects to a List of {@link DCatAPDatasetType} objects
   * @param dcatElements the list of {@link Element} objects. Not empty
   * @return the list of {@link DCatAPDatasetType} objects
   */
  public static List<DCatAPDatasetType> convertElementsToDCatList(List<Element> dcatElements) {
    List<DCatAPDatasetType> datasetTypes = new ArrayList<>(dcatElements.size());

    DatasetMarshaller dm = new DatasetMarshaller();
    dcatElements.forEach(element -> {
      DCatAPDatasetType dataset = dm.read(element);
      datasetTypes.add(dataset);
    });

    return datasetTypes;
  }

  /**
   * Converts the provided list of {@link DCatAPDatasetType} objects to a
   * list of {@link MatchType} objects
   *
   * @param datasetTypeList to be converted
   * @return the newly created list of {@link MatchType} objects
   */
  public static List<MatchType> convertDCatElementsToMatchTypes(List<DCatAPDatasetType> datasetTypeList) {

    List<MatchType> matchTypes = new ArrayList<>(datasetTypeList.size());

    datasetTypeList.forEach(dataset -> {
      MatchType matchType = new MatchType();
      matchTypes.add(matchType);


      //participant identifiers

      dataset.getPublisher().forEach(publisher -> {

        PublicOrganizationType _publisher = (PublicOrganizationType) publisher;

        com.helger.pd.searchapi.v1.IDType participantId = new com.helger.pd.searchapi.v1.IDType();
        IDType idType = _publisher.getIdAtIndex(0);
        participantId.setScheme(idType.getSchemeName());
        participantId.setValue(idType.getValue());
        //TODO: this is being done multiple times because of inversion. Find a better way.
        matchType.setParticipantID(participantId);

        EntityType entityType = new EntityType();

        AddressType addressType = _publisher.getLocationAtIndex(0).getAddress();

        entityType.setCountryCode(addressType.getAdminUnitLevel1());
        entityType.setGeoInfo(addressType.getFullAddress());

        _publisher.getPrefLabel().forEach(label -> {
          entityType.addName(new NameType(label));
        });

        matchType.addEntity(entityType);
      });

      dataset.getDistribution().forEach(dist -> {

        com.helger.pd.searchapi.v1.IDType docType = new com.helger.pd.searchapi.v1.IDType();
        String identifier = dist.getAccessService().getIdentifier();
        int index = identifier.indexOf("::");

        docType.setScheme(identifier.substring(0, index));
        docType.setValue(identifier.substring(index + 2));
        matchType.addDocTypeID(docType);
      });
    });

    return matchTypes;
  }
}
