package eu.toop.dsd.service;

import com.helger.commons.ValueEnforcer;
import com.helger.pd.searchapi.v1.MatchType;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import org.bouncycastle.pqc.crypto.gmss.GMSSStateAwareSigner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

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
