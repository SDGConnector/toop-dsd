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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.functional.IFunction;
import com.helger.commons.state.ESuccess;
import com.helger.jaxb.JAXBMarshallerHelper;
import com.helger.pd.searchapi.v1.MatchType;
import eu.toop.edm.dcatap.DCatAPDatasetMarshaller;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcatap.ObjectFactory;
import eu.toop.edm.jaxb.dcterms.DCStandardType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
        ObjectFactory of = new ObjectFactory();
        eu.toop.edm.jaxb.dcterms.ObjectFactory of2 = new eu.toop.edm.jaxb.dcterms.ObjectFactory();

        DCatAPDatasetType datasetType = of.createDCatAPDatasetType();
        final DCatAPDistributionType distributionType = of.createDCatAPDistributionType();
        distributionType.setAccessURL("http://www.google.com");
        distributionType.setByteSize(BigDecimal.TEN);

        final DCStandardType dcStandardType = of2.createDCStandardType();
        dcStandardType.setValue("conformsto");
        datasetType.addContent(of2.createConformsTo(dcStandardType));
        datasetType.addContent(of2.createTitle("thetitle"));
        datasetType.addContent(of2.createDescription("description"));
        datasetType.addContent(of.createDistribution(distributionType));

        /**
         * @XmlElementRefs({
         *         @XmlElementRef(name = "conformsTo", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "identifier", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "identifier", namespace = "http://www.w3.org/ns/adms#", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "type", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "issued", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "format", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "modified", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "title", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "description", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "page", namespace = "http://xmlns.com/foaf/0.1/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "landingPage", namespace = "http://data.europa.eu/r5r/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "language", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "creator", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "publisher", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "temporal", namespace = "http://purl.org/dc/terms/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "distribution", namespace = "http://data.europa.eu/r5r/", type = JAXBElement.class, required = false),
         *         @XmlElementRef(name = "qualifiedRelation", namespace = "http://data.europa.eu/r5r/", type = JAXBElement.class, required = false)
         *     })
         */


        final Document document = marshaller.getAsDocument(datasetType);

        dcatDocs.add(document);
      } catch (Exception e) {
        e.printStackTrace();
      }

    });

    return dcatDocs;
  }
}
