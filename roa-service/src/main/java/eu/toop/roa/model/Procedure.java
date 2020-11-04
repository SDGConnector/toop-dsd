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

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The type Procedure.
 */
@Entity
@Table(name = "procedure")
public class Procedure extends RootEntity {

  private String id;
  private String idSchemeID;

  @ManyToMany
  private List<Agent> agents;

  @ManyToMany
  private List<InformationRequirement> informationRequirements;

  /**
   * Instantiates a new Procedure.
   */
  public Procedure() {
  }

  public Procedure(String id, String idSchemeID) {
    this.id = id;
    this.idSchemeID = idSchemeID;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets id scheme id.
   *
   * @return the id scheme id
   */
  public String getIdSchemeID() {
    return idSchemeID;
  }

  /**
   * Sets id scheme id.
   *
   * @param idSchemeID the id scheme id
   */
  public void setIdSchemeID(String idSchemeID) {
    this.idSchemeID = idSchemeID;
  }

  /**
   * Gets agents.
   *
   * @return the agents
   */
  public List<Agent> getAgents() {
    return agents;
  }

  /**
   * Sets agents.
   *
   * @param agents the agents
   */
  public void setAgents(List<Agent> agents) {
    this.agents = agents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Procedure procedure = (Procedure) o;
    return Objects.equals(id, procedure.id) &&
        Objects.equals(idSchemeID, procedure.idSchemeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idSchemeID);
  }

  @Override
  public String toString() {
    return JsonHelper.toJson(this);
  }
}
