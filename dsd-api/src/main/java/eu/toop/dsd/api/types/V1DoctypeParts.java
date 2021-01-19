/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2020-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.dsd.api.types;

/**
 * Represents a ToopV1.0.0 doctype id.
 *
 * <p>sample:
 * <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40</code></p>
 *
 * @see <a href="https://github.com/TOOP4EU/toop/blob/master/Specifications/TOOP%20Policy%20for%20use%20of%20identifiers%201.0.docx">https://github.com/TOOP4EU/toop/blob/master/Specifications/TOOP%20Policy%20for%20use%20of%20identifiers%201.0.docx</a>
 * @deprecated since Toop V2.0.0 {@link V2DoctypeParts} shall be used.
 */
@Deprecated
public class V1DoctypeParts extends DoctypeParts {
  private String namespaceURI;

  private String localElementName;

  private String customizationId;

  private String v1VersionField;


  /**
   * Instantiates a new V 1 doctype parts.
   *
   * @param namespaceURI     the namespace uri
   * @param localElementName the local elemnt name
   * @param customizationId  the customization id
   * @param v1VersionField   the v 1 version field
   */
  public V1DoctypeParts(String namespaceURI, String localElementName, String customizationId, String v1VersionField) {
    this.namespaceURI = namespaceURI;
    this.localElementName = localElementName;
    this.customizationId = customizationId;
    this.v1VersionField = v1VersionField;
  }

  /**
   * Gets namespace uri.
   *
   * @return the namespace uri
   */
  public String getNamespaceURI() {
    return namespaceURI;
  }

  /**
   * Gets local elemnt name.
   *
   * @return the local elemnt name
   */
  public String getLocalElementName() {
    return localElementName;
  }

  /**
   * Gets customization id.
   *
   * @return the customization id
   */
  public String getCustomizationId() {
    return customizationId;
  }

  /**
   * Gets v 1 version field.
   *
   * @return the v 1 version field
   */
  public String getV1VersionField() {
    return v1VersionField;
  }

  @Override
  public String toString() {
    return namespaceURI + "::" + localElementName + "##" + customizationId + "::" + v1VersionField;
  }

  @Override
  public boolean matches(String datasetType) {
    //  ignore cases and underscores (CRIMINAL_RECORD = criminalRecord)
    return this.customizationId.toLowerCase()
        .contains(datasetType.replaceAll("_", "").toLowerCase());
  }

  //the methods below are only for representing the old doctype id as the new one.
  //not a health way, but its temporary and will be removed soon.

  @Override
  public String getDistributionConformsTo() {
    return customizationId;
  }

  @Override
  public String getDistributionFormat() {
    return localElementName;
  }

  @Override
  public String getConformsTo() {
    // NOTE: tentative
    return v1VersionField;
  }

  @Override
  public String getDataSetIdentifier() {
    return namespaceURI + "::" + localElementName;
  }

  @Override
  public String getDatasetType() {
    return namespaceURI;
  }
}
