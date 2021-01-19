/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
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
 * Represents the TOOP V2.0.0 doctype id.
 * <p>sample:
 * <code>RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.1</code></p>
 */
public class V2DoctypeParts extends DoctypeParts {

  /**
   * dataset identifier  e.g <code>RegisteredOrganization</code>
   */
  private String dataSetIdentifier;
  /**
   * dataset type: e.g. <code>REGISTERED_ORGANIZATION_TYPE</code>
   */
  private String datasetType;
  /**
   * distribution format: e.g. <code>CONCEPT</code>
   */
  private String distributionFormat;
  /**
   * conformance of distribution. e.g. <code>CCCEV</code>
   */
  private String distributionConformsTo;
  /**
   * doctype conforms to: e.g. <code>toop-edm:v2.1</code>
   */
  private String conformsTo;

  public V2DoctypeParts(String dataSetIdentifier, String datasetType, String distributionFormat, String distributionConformsTo, String conformsTo) {
    this.dataSetIdentifier = dataSetIdentifier;
    this.datasetType = datasetType;
    this.distributionFormat = distributionFormat;
    this.distributionConformsTo = distributionConformsTo;
    this.conformsTo = conformsTo;
  }

  /**
   * Gets data set identifier.
   *
   * @return the data set identifier
   */
  @Override
  public String getDataSetIdentifier() {
    return dataSetIdentifier;
  }

  /**
   * Gets dataset type.
   *
   * @return the dataset type
   */
  @Override
  public String getDatasetType() {
    return datasetType;
  }

  /**
   * Gets distribution format.
   *
   * @return the distribution format
   */
  @Override
  public String getDistributionFormat() {
    return distributionFormat;
  }

  /**
   * Gets distribution conforms to.
   *
   * @return the distribution conforms to
   */
  @Override
  public String getDistributionConformsTo() {
    return distributionConformsTo;
  }

  /**
   * Gets conforms to.
   *
   * @return the conforms to
   */
  @Override
  public String getConformsTo() {
    return conformsTo;
  }

  @Override
  public boolean matches(String datasetType) {
    return datasetType.equals(this.datasetType);
  }

  @Override
  public String toString() {
    return dataSetIdentifier + "::" + datasetType + "::" + distributionFormat + "::" +
        (distributionConformsTo != null ? (distributionConformsTo + "::") : "") + conformsTo;
  }
}
