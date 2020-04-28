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

import com.helger.pd.searchapi.PDSearchAPIWriter;
import com.helger.pd.searchapi.v1.MatchType;
import eu.toop.edm.dcatap.DCatAPDatasetMarshaller;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcatap.ObjectFactory;
import eu.toop.edm.jaxb.dcterms.DCMediaType;
import eu.toop.edm.jaxb.dcterms.DCStandardType;
import eu.toop.edm.jaxb.w3.locn.AddressType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

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
   * @param matchTypes the list of <code>MatchType</code> objects to be converted
   * @return the list if <code>org.w3c.dom.Document</code> objects
   */
  public static List<Document> convertBusinessCardsToDCat(List<MatchType> matchTypes) {


    final List<Document> dcatDocs = new ArrayList<>(matchTypes.size());
    matchTypes.forEach(matchType -> {
      try {
        DCatAPDatasetMarshaller marshaller = new DCatAPDatasetMarshaller();
        ObjectFactory of_dcat = new ObjectFactory();
        eu.toop.edm.jaxb.dcterms.ObjectFactory of_dcTerms = new eu.toop.edm.jaxb.dcterms.ObjectFactory();

        DCatAPDatasetType datasetType = of_dcat.createDCatAPDatasetType();
        //conformsTo
        setConformsTo(of_dcTerms, datasetType);
        //identifier
        datasetType.addContent(of_dcTerms.createIdentifier("RE238918378"));
        //type
        datasetType.addContent(of_dcTerms.createType("REGISTERED_ORGANIZATION_TYPE"));
        //title
        datasetType.addContent(of_dcTerms.createTitle("Companies registry"));
        //description
        datasetType.addContent(of_dcTerms.createDescription("A dataset about the Registered organizations"));
        //publisher
        addPublisher(matchType, of_dcTerms, datasetType);
        //distribution
        datasetType.addContent(createDistribution(matchType, of_dcat, of_dcTerms));


        final Document document = marshaller.getAsDocument(datasetType);

        dcatDocs.add(document);
      } catch (Exception e) {
        e.printStackTrace();
      }

    });

    return dcatDocs;
  }

  private static void setConformsTo(eu.toop.edm.jaxb.dcterms.ObjectFactory of_dcTerms, DCatAPDatasetType datasetType) {
    final DCStandardType dcStandardType = of_dcTerms.createDCStandardType();
    dcStandardType.setValue("REGISTERED_ORGANIZATION_ONTOLOGY_URI");
    datasetType.addContent(of_dcTerms.createConformsTo(dcStandardType));
  }

  private static JAXBElement<DCatAPDistributionType> createDistribution(MatchType matchType, ObjectFactory of_dcat, eu.toop.edm.jaxb.dcterms.ObjectFactory of_dcterms) {
    final DCatAPDistributionType distributionType = of_dcat.createDCatAPDistributionType();
    distributionType.setAccessURL("");

    final DCStandardType conformsTo = of_dcterms.createDCStandardType();
    conformsTo.setValue("CCCEV");
    distributionType.addConformsTo(conformsTo);
    final DCMediaType dcMediaType = of_dcterms.createDCMediaType();

    distributionType.setFormat(dcMediaType);
    return of_dcat.createDistribution(distributionType);
  }

  private static void addPublisher(MatchType matchType,
                                   eu.toop.edm.jaxb.dcterms.ObjectFactory of_dcTerms,
                                   DCatAPDatasetType datasetType) {

    /*
    <dct:publisher xsi:type="cagv:PublicOrganizationType">
        <cbc:id schemeID="VAT">DE730757727</cbc:id>
        <cagv:location xsi:type="locn:AddressType">
            <locn:fullAddress>Prince Street 15</locn:fullAddress>
            <locn:adminUnitLevel1>GB</locn:adminUnitLevel1>
        </cagv:location>
        <skos:prefLabel>PublisherName</skos:prefLabel>
    </dct:publisher>
     */
    eu.toop.edm.jaxb.cv.agent.ObjectFactory of_cvAgent = new eu.toop.edm.jaxb.cv.agent.ObjectFactory();

    //<cbc:id schemeID="VAT">DE730757727</cbc:id>
    eu.toop.edm.jaxb.cv.cbc.ObjectFactory of_cbc = new eu.toop.edm.jaxb.cv.cbc.ObjectFactory();
    final IDType idType = of_cbc.createIDType();
    idType.setSchemeName("VAT");
    idType.setValue("DE730757727");
    of_cbc.createId(idType);

    /*
     <cagv:location xsi:type="locn:AddressType">
            <locn:fullAddress>Prince Street 15</locn:fullAddress>
            <locn:adminUnitLevel1>GB</locn:adminUnitLevel1>
        </cagv:location>
     */
    eu.toop.edm.jaxb.w3.locn.ObjectFactory of_address = new eu.toop.edm.jaxb.w3.locn.ObjectFactory();
    final AddressType addressType = of_address.createAddressType();
    addressType.setFullAddress("Prince Street 15");
    addressType.setAdminUnitLevel1("GB");


    PublicOrganizationType publicOrganizationType = of_cvAgent.createPublicOrganizationType();
    publicOrganizationType.getPrefLabel().add(matchType.getEntity().get(0).getName().toString());
    final eu.toop.edm.jaxb.cv.cbc.NameType nameType = of_cbc.createNameType();
    nameType.setValue(matchType.getEntity().get(0).getName().get(0).getValue());
    publicOrganizationType.addFormalName(nameType);
    datasetType.addContent(of_dcTerms.createPublisher(publicOrganizationType));
  }
}
