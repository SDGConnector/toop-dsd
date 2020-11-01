package eu.toop.roa.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address extends RootEntity {
  private String fullAddress;
  private String streetName;
  private String buildingNumber;
  private String town;
  private String postalCode;
  private String countryCode;

  public Address() {
  }

  public Address(String fullAddress, String streetName, String buildingNumber, String town, String postalCode, String countryCode) {
    this.fullAddress = fullAddress;
    this.streetName = streetName;
    this.buildingNumber = buildingNumber;
    this.town = town;
    this.postalCode = postalCode;
    this.countryCode = countryCode;
  }

  public String getFullAddress() {
    return fullAddress;
  }

  public void setFullAddress(String fullAddress) {
    this.fullAddress = fullAddress;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getBuildingNumber() {
    return buildingNumber;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }
}
