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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * A class to write DSD responses
 *
 * @author yerlibilgin
 */
public class DsdDataConverter {
  private static final Transformer transformer;
  private static final Transformer inverseTransformer;

  static {
    InputStream stylesheet = DsdDataConverter.class.getResourceAsStream("/xslt/dsd.xslt");
    StreamSource stylesource = new StreamSource(stylesheet);
    try {
      transformer = TransformerFactory.newInstance().newTransformer(stylesource);
    } catch (TransformerConfigurationException e) {
      throw new IllegalStateException("Cannot instantiate transformer");
    }

    stylesheet = DsdDataConverter.class.getResourceAsStream("/xslt/dsd-inverse.xslt");
    stylesource = new StreamSource(stylesheet);
    try {
      inverseTransformer = TransformerFactory.newInstance().newTransformer(stylesource);
    } catch (TransformerConfigurationException e) {
      throw new IllegalStateException("Cannot instantiate transformer");
    }
  }

  /**
   * Converts a DIR result to a DSD result
   *
   * @param directoryResult the xml received from the toop directory
   * @param datasetType     the optional datasetType parameter, used for filtering the record
   * @param dpType          the dpType query parameter for filtering and returning only the selected entities
   * @return
   */
  public static String convertDIRToDSDWithDPType(String directoryResult, String datasetType, String dpType) throws TransformerException {
    return convertDIRToDSD(directoryResult, datasetType, null, dpType);
  }

  /**
   * Converts a DIR result to a DSD result
   *
   * @param directoryResult the xml received from the toop directory
   * @param datasetType     the optional datasetType parameter, used for filtering the record
   * @param countryCode     the country code for filtering and returning only the selected countries
   * @return
   */
  public static String convertDIRToDSDWithCountryCode(String directoryResult, String datasetType, String countryCode) throws TransformerException {
    return convertDIRToDSD(directoryResult, datasetType, countryCode, null);
  }

  private static String convertDIRToDSD(String directoryResult, String datasetType, String countryCode, String dpType) throws TransformerException {
    transformer.clearParameters();
    if (datasetType != null)
      transformer.setParameter("datasetType", datasetType);

    if (countryCode != null)
      transformer.setParameter("countryCode", countryCode);

    if (dpType != null)
      transformer.setParameter("dpType", dpType);

    StringWriter writer = new StringWriter();
    StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(directoryResult.getBytes(StandardCharsets.UTF_8)));
    transformer.transform(xmlSource, new StreamResult(writer));

    return writer.toString();
  }

  /**
   * Convert a Regrep/DCAT DSD xml document to a Directory result list
   *
   * @param dsdXml
   * @return the directory resultlist xml
   * @throws TransformerException
   */
  public static String convertDSDToToopDirResultList(String dsdXml) throws TransformerException {
    StringWriter writer = new StringWriter();
    StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(dsdXml.getBytes(StandardCharsets.UTF_8)));
    inverseTransformer.transform(xmlSource, new StreamResult(writer));
    return writer.toString();
  }
}
