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
import com.helger.pd.searchapi.v1.MatchType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

/**
 * This class is responsible for converting the TOOP Directory query result into 
 * the DSD BregDCAT Profile documents.
 *
 * @author @yerlibilgin
 */
public class BregDCatHelper {

  private static final DocumentBuilder DOCUMENT_BUILDER;

  private static Map<String, String> _namespaceMap = new HashMap<>();

  private static final String DCAT = "dcat";
  private static final String DCT = "dct";
  private static final String SKOS = "skos";
  private static final String FOAF = "foaf";
  private static final String CVA = "cva";
  private static final String CVB = "cvb";

  static {
    _namespaceMap.put(DCAT, "http://data.europa.eu/r5r/");
    _namespaceMap.put(DCT, "http://purl.org/dc/terms/");
    _namespaceMap.put(SKOS, "http://www.w3.org/2004/02/skos/core#");
    _namespaceMap.put(FOAF, "http://xmlns.com/foaf/0.1/");
    _namespaceMap.put(CVA, "http://www.w3.org/ns/corevocabulary/AggregateComponents");
    _namespaceMap.put(CVB, "http://www.w3.org/ns/corevocabulary/BasicComponents");


    final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);
    try {
      DOCUMENT_BUILDER = documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

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
      Document document = DOCUMENT_BUILDER.newDocument();
      final Element dataset = _createElement(document, DCAT, "Dataset");

      _namespaceMap.forEach((prefix, namespace) -> {
        dataset.setAttribute("xmlns:" + prefix, namespace);
      });

      document.appendChild(dataset);

      //TODO: convert the matchtype to breg dcat

      Element title = _createElement(document, DCT, "title");
      title.setTextContent(matchType.getEntity().get(0).getName().get(0).getValue());
      dataset.appendChild(title);
      dcatDocs.add(document);
    });

    return dcatDocs;
  }


  private static final Element _createElement(Document document, String prefix, String tagName) {
    ValueEnforcer.notNull(document, "document");
    ValueEnforcer.notNull(tagName, "tagName");

    if (prefix == null || prefix.isEmpty()) {
      return document.createElement(tagName);
    } else {
      String sNamespace = _namespaceMap.get(prefix);

      if (sNamespace == null)
        throw new IllegalArgumentException("No namespace defined for " + prefix);

      return document.createElementNS(sNamespace, prefix + ":" + tagName);
    }

  }
}
