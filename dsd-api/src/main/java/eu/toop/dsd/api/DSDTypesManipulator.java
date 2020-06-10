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
import com.helger.peppolid.CIdentifier;
import eu.toop.dsd.api.types.DoctypeFormatException;
import eu.toop.dsd.api.types.DoctypeParts;
import eu.toop.edm.jaxb.cv.agent.LocationType;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDataServiceType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcterms.DCMediaType;
import eu.toop.edm.jaxb.dcterms.DCStandardType;
import eu.toop.edm.jaxb.w3.locn.AddressType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible for manipulating various types that DSD uses.
 *
 * @author yerlibilgin
 */
public class DSDTypesManipulator {
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDTypesManipulator.class);

  /**
   * Converts the {@link MatchType} objects that are obtained from the TOOP Directory into <code>org.w3c.dom.Document</code>
   * objects that are of type {@link DCatAPDatasetType}.
   *
   * @param s_DataSetType Dataset type
   * @param matchTypes    the list of <code>MatchType</code> objects to be converted
   * @return the list if <code>org.w3c.dom.Document</code> objects
   */
  public static List<Document> convertMatchTypesToDCATDocuments(final String s_DataSetType, List<MatchType> matchTypes) {

    LOGGER.debug("DatasetType: " + s_DataSetType);

    final List<Document> dcatDocs = new ArrayList<>(matchTypes.size());
    matchTypes.forEach(matchType -> {
      matchType.getDocTypeID().forEach(docType -> {
        //make sure we work on some real doctypes and entities
        if (matchType.getDocTypeID().size() > 0 && matchType.getEntity().size() > 0) {
          DatasetMarshaller marshaller = new DatasetMarshaller();

          DCatAPDatasetType datasetType = new DCatAPDatasetType();

          final String s_docType = flattenIdType(docType);

          DoctypeParts parts = DoctypeParts.parse(s_docType);


          //conformsTo
          setConformsTo(datasetType, "?" + s_DataSetType + "_ONTOLOGY_URI?");
          //identifier
          datasetType.addIdentifier(parts.getDataSetIdentifier());


          //type
          datasetType.setType(s_DataSetType);
          //title
          datasetType.addTitle("?" + s_DataSetType + " dataset?");
          //description
          datasetType.addDescription("?A dataset for " + s_DataSetType + "?");
          //publisher
          addPublishers(matchType, datasetType);
          //distribution
          addDistribution(docType, datasetType, s_DataSetType, parts);
          final Document document = marshaller.getAsDocument(datasetType);
          dcatDocs.add(document);
        }
      });
    });

    return dcatDocs;
  }

  private static void addDistribution(com.helger.pd.searchapi.v1.IDType docType, DCatAPDatasetType datasetType, String s_dataSetType, DoctypeParts doctypeParts) {
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

    //<DatasetIdentifier>::<DatasetType>::<Distribution.Format>[##<Distribution.ConformsTo>]::<Dataservice.ConformsTo>

    final DCatAPDistributionType distributionType = new DCatAPDistributionType();
    distributionType.setAccessURL("");

    if(doctypeParts.getDistributionConformsTo() != null) {
      final DCStandardType conformsTo = new DCStandardType();
      //<dct:conformsTo>RegRepv4-EDMv2</dct:conformsTo>
      conformsTo.setValue(doctypeParts.getDistributionConformsTo());
      distributionType.addConformsTo(conformsTo);
    }

    //<dct:format>UNSTRUCTURED</dct:format>
    final DCMediaType dcMediaType = new DCMediaType();

    dcMediaType.addContent(doctypeParts.getDistributionFormat());

    distributionType.setFormat(dcMediaType);

    //<dct:description>This is a pdf distribution of the Criminal Record</dct:description>
    distributionType.addDescription("?This is a distribution of " + s_dataSetType + "?");

    // <dcat:accessService>
    //   <!-- doctypeid -->
    //   <dct:identifier>toop-doctypeid-qns::urn:eu:toop:ns:dataexchange-2::Request##urn:eu.toop.request.criminalRecord::2.0</dct:identifier>
    //   <dct:title>Access Service Title</dct:title>
    // </dcat:accessService>
    DCatAPDataServiceType accessService = new DCatAPDataServiceType();
    accessService.setIdentifier(flattenIdType(docType));
    accessService.setTitle("?Service for " + s_dataSetType + " distribution?");
    accessService.addEndpointURL("");
    accessService.addConformsTo(new DCStandardType(doctypeParts.getConformsTo()));
    distributionType.setAccessService(accessService);

    //<dcat:mediaType>application/pdf</dcat:mediaType>
    DCMediaType mediaType = new DCMediaType();
    mediaType.addContent("?application/xml?");
    distributionType.setMediaType(mediaType);

    datasetType.addDistribution(distributionType);

  }

  private static void setConformsTo(DCatAPDatasetType datasetType, String conformsTo) {
    final DCStandardType dcStandardType = new DCStandardType();
    dcStandardType.setValue(conformsTo);
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
      PublicOrganizationType publisher = new PublicOrganizationType();

      //<cbc:id schemeID="VAT">DE730757727</cbc:id>
      final IDType idType = new IDType();
      idType.setSchemeName(matchType.getParticipantID().getScheme());
      idType.setValue(matchType.getParticipantIDValue());
      publisher.addId(idType);

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
      publisher.addLocation(locationType);

      //<skos:prefLabel>PublisherName</skos:prefLabel> (for each name type)
      final List<NameType> entityNames = entityType.getName();
      entityNames.forEach(nameType -> {
        publisher.getPrefLabel().add(nameType.getValue());
      });
      datasetType.addPublisher(publisher);
    });
  }


  /**
   * Convert the given <code>idType</code> to <code>scheme::value</code> format.
   *
   * @param idType
   * @return
   */
  public static String flattenIdType(com.helger.pd.searchapi.v1.IDType idType) {
    return CIdentifier.getURIEncoded(idType.getScheme(), idType.getValue());
  }

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
   * Inverse of {@link DSDTypesManipulator#convertMatchTypesToDCATDocuments}. Converts the provided
   * list of {@link DCatAPDatasetType} objects to a list of {@link MatchType}
   * objects
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

  /**
   * <p>
   * TODO: not a good code. Modifies the underlying lists as well.
   * <p>
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
   * @param sCountryCode  country code
   * @param matchTypes    Match types
   */
  public static void filterDirectoryResults(String s_datasetType, String sCountryCode, List<MatchType> matchTypes) {
    //filter
    final Iterator<MatchType> iterator = matchTypes.iterator();

    while (iterator.hasNext()) {
      MatchType matchType = iterator.next();
      final Iterator<com.helger.pd.searchapi.v1.IDType> iterator1 = matchType.getDocTypeID().iterator();
      while (iterator1.hasNext()) {
        com.helger.pd.searchapi.v1.IDType idType = iterator1.next();

        final String s_docType = DSDTypesManipulator.flattenIdType(idType);
        try {
          DoctypeParts parts = DoctypeParts.parse(s_docType);
          //toop-doctypeid-qns::
          //RegisteredOrganization::
          //REGISTERED_ORGANIZATION_TYPE::
          //CONCEPT##CCCEV::
          //toop-edm:v2.0
          // or the old doctype:
          //toop-doctypeid-qns::
          //urn:eu:toop:ns:dataexchange-1p40::
          //Response##urn:eu.toop.request.registeredorganization-list::
          //1.40
          // TODO: This is temporary, for now we are removing _ (underscore) and performing a case insensitive "contains" search

          //first check for the EXACT match

          if(!parts.matches(s_datasetType)){
            iterator1.remove();
          }
        } catch (DoctypeFormatException ex) {
          LOGGER.warn("Skipping doctype: " + s_docType);
          iterator1.remove();
        }
      }

      // if all doctypes have been removed then, eliminate this business card
      if (matchType.getDocTypeID().size() == 0) {
        iterator.remove();
        continue;
      }

      if (sCountryCode != null) {
        final List<EntityType> entity = matchType.getEntity();
        final Iterator<EntityType> iterator2 = entity.iterator();
        while (iterator2.hasNext()) {
          EntityType entityType = iterator2.next();
          if (!entityType.getCountryCode().equals(sCountryCode)) {
            iterator2.remove();
          }
        }
        if (matchType.getEntity().isEmpty()) {
          iterator.remove();
        }
      }
    }
  }
}
