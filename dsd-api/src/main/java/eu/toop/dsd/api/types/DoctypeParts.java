/**
 * Copyright (C) 2018-2020 toop.eu
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
 */
package eu.toop.dsd.api.types;

/**
 * This class is a representation of a doctype splitted by the :: <br>
 * <p>
 * A sample doctype:<br>
 * "toop-doctypeid-qns::RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.0");
 *
 * @author yerlibilgin
 */
public class DoctypeParts {
  /**
   * doctype scheme. e.g <code>toop-doctypeid-qns</code>
   */
  private String scheme;

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
   * doctype confroms to: e.g. <code>toop-edm:v2.0</code>
   */
  private String conformsTo;


  /**
   * returns dataSetIdentifier
   *
   * @param dataSetIdentifier the data set identifier
   */
  public void setDataSetIdentifier(String dataSetIdentifier) {
    this.dataSetIdentifier = dataSetIdentifier;
  }

  /**
   * Gets data set identifier.
   *
   * @return the data set identifier
   */
  public String getDataSetIdentifier() {
    return dataSetIdentifier;
  }

  /**
   * Sets dataset type.
   *
   * @param datasetType the dataset type
   */
  public void setDatasetType(String datasetType) {
    this.datasetType = datasetType;
  }

  /**
   * Gets dataset type.
   *
   * @return the dataset type
   */
  public String getDatasetType() {
    return datasetType;
  }

  /**
   * Sets distribution format.
   *
   * @param distributionFormat the distribution format
   */
  public void setDistributionFormat(String distributionFormat) {
    this.distributionFormat = distributionFormat;
  }

  /**
   * Gets distribution format.
   *
   * @return the distribution format
   */
  public String getDistributionFormat() {
    return distributionFormat;
  }

  /**
   * Sets distribution conforms to.
   *
   * @param distributionConformsTo the distribution conforms to
   */
  public void setDistributionConformsTo(String distributionConformsTo) {
    this.distributionConformsTo = distributionConformsTo;
  }

  /**
   * Gets distribution conforms to.
   *
   * @return the distribution conforms to
   */
  public String getDistributionConformsTo() {
    return distributionConformsTo;
  }

  /**
   * Sets conforms to.
   *
   * @param conformsTo the conforms to
   */
  public void setConformsTo(String conformsTo) {
    this.conformsTo = conformsTo;
  }

  /**
   * Gets conforms to.
   *
   * @return the conforms to
   */
  public String getConformsTo() {
    return conformsTo;
  }

  /**
   * A temporary method tho check whether the doctype is the new version
   *
   * @param s_docType string to parse
   * @return The parsed doctype parts
   */
  public static DoctypeParts parse(String s_docType) {
    //<DatasetIdentifier>::<DatasetType>::<Distribution.Format>[##<Distribution.ConformsTo>]::<Dataservice.ConformsTo>
    DoctypeParts doctypeParts = new DoctypeParts();
    final String[] split = s_docType.split("::");
    String distribution;
    if (split.length == 4) {
      //dataset identifier does not contain scheme
      doctypeParts.setDataSetIdentifier(split[0]);
      doctypeParts.setDatasetType(split[1]);
      distribution = split[2];
      doctypeParts.setConformsTo(split[3]);
    } else if (split.length == 5) {
//dataset identifier does not contain scheme
      doctypeParts.setScheme(split[0]);
      doctypeParts.setDataSetIdentifier(split[1]);
      doctypeParts.setDatasetType(split[2]);
      distribution = split[3];
      doctypeParts.setConformsTo(split[4]);
    } else {
      throw new IllegalArgumentException("Invalid doctype " + s_docType);
    }

    if (distribution.contains("##")) {
      final int i = distribution.indexOf("##");
      String distConformsTo = distribution.substring(i + 2);
      distribution = distribution.substring(0, i);
      doctypeParts.setDistributionFormat(distribution);
      doctypeParts.setDistributionConformsTo(distConformsTo);
    } else {
      doctypeParts.setDistributionFormat(distribution);
    }

    return doctypeParts;
  }

  /**
   * Gets scheme.
   *
   * @return the scheme
   */
  public String getScheme() {
    return scheme;
  }

  /**
   * Sets scheme.
   *
   * @param scheme the scheme
   */
  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  @Override
  public String toString() {
    return "DoctypeParts{" +
        "scheme='" + scheme + '\'' +
        ", dataSetIdentifier='" + dataSetIdentifier + '\'' +
        ", datasetType='" + datasetType + '\'' +
        ", distributionFormat='" + distributionFormat + '\'' +
        ", distributionConformsTo='" + distributionConformsTo + '\'' +
        ", conformsTo='" + conformsTo + '\'' +
        '}';
  }
}
