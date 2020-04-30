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
package eu.toop.dsd.service;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.helger.pd.searchapi.v1.MatchType;

import eu.toop.edm.jaxb.cv.agent.LocationType;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcterms.DCMediaType;
import eu.toop.edm.jaxb.dcterms.DCStandardType;
import eu.toop.edm.jaxb.w3.locn.AddressType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;

/**
 * This class is responsible for converting the TOOP Directory query result into
 * the DSD BregDCAT Profile documents.
 *
 * @author @yerlibilgin
 */
public class BregDCatHelper {

  /**
   * Converts the BusinessCards that are obtained from the TOOP Directory
   * as <code>MatchType</code> instances into <code>org.w3c.dom.Document</code>
   * objects compatible with the DSD BregDcatAP.
   *
   *
   * @param s_DataSetType
   * @param matchTypes the list of <code>MatchType</code> objects to be converted
   * @return the list if <code>org.w3c.dom.Document</code> objects
   */
  public static List<Document> convertBusinessCardsToDCat(final String s_DataSetType, List<MatchType> matchTypes) {


    final List<Document> dcatDocs = new ArrayList<>(matchTypes.size());
    matchTypes.forEach(matchType -> {
        DatasetMarshaller marshaller = new DatasetMarshaller();

        DCatAPDatasetType datasetType = new DCatAPDatasetType();
        //conformsTo
        setConformsTo(datasetType);
        //identifier
        datasetType.addIdentifier ("RE238918378");
        //type
        datasetType.setType (s_DataSetType);
        //title
        datasetType.addTitle ("?title?");
        //description
        datasetType.addDescription ("?description?");
        //publisher
        addPublisher(matchType, datasetType);
        //distribution
        datasetType.addDistribution (createDistribution(matchType));
        final Document document = marshaller.getAsDocument(datasetType);
        dcatDocs.add(document);

    });

    return dcatDocs;
  }

  private static void setConformsTo( DCatAPDatasetType datasetType) {
    final DCStandardType dcStandardType =  new DCStandardType();
    dcStandardType.setValue("REGISTERED_ORGANIZATION_ONTOLOGY_URI");
    datasetType.addConformsTo (dcStandardType);
  }

  private static DCatAPDistributionType createDistribution(MatchType matchType) {
    final DCatAPDistributionType distributionType = new DCatAPDistributionType();
    distributionType.setAccessURL("");

    final DCStandardType conformsTo =  new DCStandardType();
    conformsTo.setValue("CCCEV");
    distributionType.addConformsTo(conformsTo);
    final DCMediaType dcMediaType = new DCMediaType();

    distributionType.setFormat(dcMediaType);
    return distributionType;
  }

  private static void addPublisher(MatchType matchType,
                                   DCatAPDatasetType datasetType) {
    /*
    <dct:publisher xsi:type="cagv:PublicOrganizationType">
        <cbc:id schemeID="VAT">DE730757727</cbc:id>
        <cagv:location>
           <cagv:address>
               <locn:fullAddress>Prince Street 15</locn:fullAddress>
               <locn:adminUnitLevel1>GB</locn:adminUnitLevel1>
           </cagv:address>
        </cagv:location>
        <skos:prefLabel>PublisherName</skos:prefLabel>
    </dct:publisher>
     */
    //<dct:publisher xsi:type="cagv:PublicOrganizationType">
    PublicOrganizationType publicOrganizationType = new PublicOrganizationType();

    //<cbc:id schemeID="VAT">DE730757727</cbc:id>
    final IDType idType = new IDType();
    idType.setSchemeName(matchType.getParticipantID().getScheme());
    idType.setValue(matchType.getParticipantIDValue());
    publicOrganizationType.addId(idType);

    /*
       <cagv:location>
           <cagv:address>
               <locn:fullAddress>Prince Street 15</locn:fullAddress>
               <locn:adminUnitLevel1>GB</locn:adminUnitLevel1>
           </cagv:address>
        </cagv:location>
     */
    final AddressType addressType = new AddressType();
    addressType.setFullAddress("Prince Street 15");
    addressType.setAdminUnitLevel1("GB");
    final LocationType locationType = new LocationType();
    locationType.setAddress(addressType);
    publicOrganizationType.addLocation(locationType);


    //<skos:prefLabel>PublisherName</skos:prefLabel>
    publicOrganizationType.getPrefLabel().add(matchType.getEntity().get(0).getName().get(0).getValue());

    datasetType.addPublisher (publicOrganizationType);
  }
}
