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

import com.helger.pd.searchapi.v1.NameType;
import eu.toop.edm.jaxb.dcatap.DCatAPDataServiceType;
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
   * @param s_DataSetType
   * @param matchTypes    the list of <code>MatchType</code> objects to be converted
   * @return the list if <code>org.w3c.dom.Document</code> objects
   */
  public static List<Document> convertBusinessCardsToDCat(final String s_DataSetType, List<MatchType> matchTypes) {


    final List<Document> dcatDocs = new ArrayList<>(matchTypes.size());
    matchTypes.forEach(matchType -> {
      //make sure we work on some real doctypes and entities
      if (matchType.getDocTypeID().size() > 0 && matchType.getEntity().size() > 0) {
        DatasetMarshaller marshaller = new DatasetMarshaller();

        DCatAPDatasetType datasetType = new DCatAPDatasetType();
        //conformsTo
        setConformsTo(datasetType, s_DataSetType);
        //identifier
        datasetType.addIdentifier(ToopDirClient.flattenIdType(matchType.getParticipantID()));
        //type
        datasetType.setType(s_DataSetType);
        //title
        datasetType.addTitle(s_DataSetType + " dataset");
        //description
        datasetType.addDescription("A dataset for " + s_DataSetType);
        //publisher
        addPublishers(matchType, datasetType);
        //distribution
        addDistributions(matchType, datasetType, s_DataSetType);
        final Document document = marshaller.getAsDocument(datasetType);
        dcatDocs.add(document);
      }
    });

    return dcatDocs;
  }

  private static void addDistributions(MatchType matchType, DCatAPDatasetType datasetType, String s_dataSetType) {
    /*
    <dcat:distribution>
      <dct:conformsTo>RegRepv4-EDMv2</dct:conformsTo>
      <dct:description>This is a pdf distribution of the Criminal Record</dct:description>
      <dct:format>UNSTRUCTURED</dct:format>
      <dcat:accessService>
        <!-- doctypeid -->
        <dct:identifier>toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-2::Request##urn:eu.toop.request.criminalRecord::2.0</dct:identifier>
        <dct:title>Access Service Title</dct:title>
      </dcat:accessService>
      <dcat:mediaType>application/pdf</dcat:mediaType>
    </dcat:distribution>
     */

    matchType.getDocTypeID().forEach(idType -> {
      final DCatAPDistributionType distributionType = new DCatAPDistributionType();
      distributionType.setAccessURL("");

      final DCStandardType conformsTo = new DCStandardType();
      //<dct:conformsTo>RegRepv4-EDMv2</dct:conformsTo>
      conformsTo.setValue("CCCEV");
      distributionType.addConformsTo(conformsTo);

      //<dct:format>UNSTRUCTURED</dct:format>
      final DCMediaType dcMediaType = new DCMediaType();
      dcMediaType.addContent("UNSTRUCTURED");
      distributionType.setFormat(dcMediaType);

      //<dct:description>This is a pdf distribution of the Criminal Record</dct:description>
      distributionType.addDescription("This is a distribution of " + s_dataSetType);

      // <dcat:accessService>
      //   <!-- doctypeid -->
      //   <dct:identifier>toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-2::Request##urn:eu.toop.request.criminalRecord::2.0</dct:identifier>
      //   <dct:title>Access Service Title</dct:title>
      // </dcat:accessService>
      DCatAPDataServiceType accessService = new DCatAPDataServiceType();
      accessService.setIdentifier(ToopDirClient.flattenIdType(idType));
      accessService.setTitle("Service for " + s_dataSetType + " distribution");
      accessService.addEndpointURL("");
      distributionType.setAccessService(accessService);

      //<dcat:mediaType>application/pdf</dcat:mediaType>
      DCMediaType mediaType = new DCMediaType();
      mediaType.addContent("application/xml");
      distributionType.setMediaType(mediaType);

      datasetType.addDistribution(distributionType);
    });
  }

  private static void setConformsTo(DCatAPDatasetType datasetType, String s_DataSetType) {
    final DCStandardType dcStandardType = new DCStandardType();
    dcStandardType.setValue(s_DataSetType + "_ONTOLOGY_URI");
    datasetType.addConformsTo(dcStandardType);
  }

  private static void addPublishers(MatchType matchType,
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

    matchType.getEntity().forEach(entityType -> {
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

      addressType.setAdminUnitLevel1(entityType.getCountryCode());
      addressType.setFullAddress(entityType.getGeoInfo());

      final LocationType locationType = new LocationType();
      locationType.setAddress(addressType);
      publicOrganizationType.addLocation(locationType);

      //<skos:prefLabel>PublisherName</skos:prefLabel> (for each name type)
      final List<NameType> entityNames = entityType.getName();
      entityNames.forEach(nameType -> {
        publicOrganizationType.getPrefLabel().add(nameType.getValue());
      });
      datasetType.addPublisher(publicOrganizationType);
    });
  }
}
