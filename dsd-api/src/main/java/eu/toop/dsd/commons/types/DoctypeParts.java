package eu.toop.dsd.commons.types;

public class DoctypeParts {
  private String scheme;
  private String dataSetIdentifier;
  private String datasetType;
  private String distributionFormat;
  private String distributionConformsTo;
  private String conformsTo;


  public void setDataSetIdentifier(String dataSetIdentifier) {
    this.dataSetIdentifier = dataSetIdentifier;
  }

  public String getDataSetIdentifier() {
    return dataSetIdentifier;
  }

  public void setDatasetType(String datasetType) {
    this.datasetType = datasetType;
  }

  public String getDatasetType() {
    return datasetType;
  }

  public void setDistributionFormat(String distributionFormat) {
    this.distributionFormat = distributionFormat;
  }

  public String getDistributionFormat() {
    return distributionFormat;
  }

  public void setDistributionConformsTo(String distributionConformsTo) {
    this.distributionConformsTo = distributionConformsTo;
  }

  public String getDistributionConformsTo() {
    return distributionConformsTo;
  }

  public void setConformsTo(String conformsTo) {
    this.conformsTo = conformsTo;
  }

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

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }
}
