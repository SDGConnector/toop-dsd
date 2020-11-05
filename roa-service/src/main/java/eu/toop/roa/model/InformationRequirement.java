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
package eu.toop.roa.model;

import eu.toop.roa.util.JsonHelper;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "information_requirement")
public class InformationRequirement extends RootEntity{

  private String idValue;
  private String idSchemeID;
  private boolean legalMandate;

  @ManyToMany
  private List<Procedure> procedures;

  public InformationRequirement() {
  }

  public InformationRequirement(String idValue, String idSchemeID, boolean legalMandate) {
    this.idValue = idValue;
    this.idSchemeID = idSchemeID;
    this.legalMandate = legalMandate;
  }

  public String getIdValue() {
    return idValue;
  }

  public void setIdValue(String idValue) {
    this.idValue = idValue;
  }

  public String getIdSchemeID() {
    return idSchemeID;
  }

  public void setIdSchemeID(String idSchemeID) {
    this.idSchemeID = idSchemeID;
  }

  public boolean isLegalMandate() {
    return legalMandate;
  }

  public void setLegalMandate(boolean legalMandate) {
    this.legalMandate = legalMandate;
  }

  public List<Procedure> getProcedures() {
    return procedures;
  }

  public void setProcedures(List<Procedure> procedures) {
    this.procedures = procedures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InformationRequirement that = (InformationRequirement) o;
    return legalMandate == that.legalMandate &&
        Objects.equals(idValue, that.idValue) &&
        Objects.equals(idSchemeID, that.idSchemeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idValue, idSchemeID, legalMandate);
  }

  @Override
  public String toString() {
    return JsonHelper.toJson(this);
  }
}
