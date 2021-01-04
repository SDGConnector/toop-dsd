/**
 * Copyright (C) 2018-2021 toop.eu
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

import com.helger.commons.ValueEnforcer;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;

/**
 * Parses a given flat doctype id String as either a {@link V1DoctypeParts} or a {@link V2DoctypeParts} object.
 * @author yerlibilgin
 */
public abstract class DoctypeParts {

  /**
   * Returns the version of this identifier.
   * Either 1.0 or 2.0
   */
  private String version;

  public String getVersion() {
    return version;
  }


  /**
   * A temporary method tho check whether the doctype is the new version
   *
   * @param s_docType string to parse
   * @return The parsed doctype parts
   */
  public static DoctypeParts parse(String s_docType) {

    ValueEnforcer.notEmpty(s_docType, "doctype");


    //remove the scheme if it is there
    final String schemePrefix = EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME + "::";
    if (s_docType.startsWith(schemePrefix)){
      s_docType = s_docType.substring(schemePrefix.length());
    }

    final String[] split = s_docType.split("::");

    if (split.length == 3) {
      //Version 1.0

      String namespace = split[0];
      String middle = split[1];


      int indexofDoubleHash = middle.indexOf("##");
      if (indexofDoubleHash == -1)
        throw new IllegalArgumentException("Invalid doctype " + s_docType);

      String localElementName = middle.substring(0, indexofDoubleHash);
      String customizationId = middle.substring(indexofDoubleHash + 2);
      String v1VersionField = split[2];

      return new V1DoctypeParts(namespace, localElementName, customizationId, v1VersionField);
    } else if (split.length == 4) {
      //Version 2.0
      String datasetIdentifier = split[0];
      String datasetType = split[1];
      String conformsTo = split[3];
      String distributionFormat = split[2];
      String distConformsTo = null;
      if (distributionFormat.contains("##")) {
        final int i = distributionFormat.indexOf("##");
        distConformsTo = distributionFormat.substring(i + 2);
        distributionFormat = distributionFormat.substring(0, i);
      }
      return new V2DoctypeParts(datasetIdentifier, datasetType, distributionFormat, distConformsTo, conformsTo);
    } else {
      throw new DoctypeFormatException("Invalid doctype " + s_docType);
    }
  }

  /**
   * Check this doctype against the given dataset type and return <code>true</code> if
   * it matches
   *
   * @param datasetType
   * @return true if the datasetType matches this doctype.
   */
  public abstract boolean matches(String datasetType);

  public abstract String getDistributionConformsTo();

  public abstract String getDistributionFormat();

  public abstract String getConformsTo();

  public abstract String getDataSetIdentifier();

  public abstract String getDatasetType();
}
