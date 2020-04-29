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

import java.math.BigInteger;
import java.util.List;

import org.w3c.dom.Document;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.SlotBuilder;
import eu.toop.regrep.query.QueryResponse;

/**
 * A simple RegRep helper that wraps data into RegRep slots.
 *
 * @author @yerlibilgin
 */
public class DSDRegRep
{
  //FIXME muhammet: I added these here because it wasn't being able to
  // serialize the cagv:PublicOrganizationType as dct:publisher because
  // it couldn't validate against the xsd (not being able to find it).
  // however this is the 'regrep' module and these xsds shouldn't be
  // here. We need to find a better place for them.
  private static final ClassPathResource[] ADDITIONAL_XSDS = CCAGV.XSDS.toArray (new ClassPathResource [0]); 
//  CRegRep4.allXSDsQueryList.add(new ClassPathResource("schemas/CV-Agent.xsd",CRegRep4.class.getClassLoader()));
//  CRegRep4.allXSDsQueryList.add(new ClassPathResource("schemas/dcterms.xsd",CRegRep4.class.getClassLoader()));
//  CRegRep4.allXSDsQueryList.add(new ClassPathResource("schemas/locn.xsd", CRegRep4.class.getClassLoader()));

  /**
   * Wrap the given list of <code>org.w3c.dom.Document</code> objects into
   * RegRep slots of <code>AnyValueType</code> and returns the created
   * <code>QueryResponse</code> as a <code>String</code>
   *
   * @param sReqesutID
   *        the request identifier, obtained from the QueryRequest
   * @param dcatDocuments
   *        the list of dcat documents to be contained in slots
   * @return the QueryResponse as a String
   */
  public static String createQueryResponse (final String sReqesutID, final List <Document> dcatDocuments)
  {

    final QueryResponse aQResponse = RegRepHelper.createQueryResponse (sReqesutID);

    dcatDocuments.forEach (dcatDocument -> {
      aQResponse.addSlot (new SlotBuilder ().setName ("Dataset")
                                            .setValue (dcatDocument.getDocumentElement ())
                                            .build ());
    });

    aQResponse.setTotalResultCount (BigInteger.valueOf (dcatDocuments.size ()));
    aQResponse.setStartIndex (BigInteger.ZERO);
    return RegRep4Writer.queryResponse (ADDITIONAL_XSDS).setFormattedOutput (true).getAsString (aQResponse);
  }
}
