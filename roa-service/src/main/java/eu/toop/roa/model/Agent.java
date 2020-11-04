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


import io.ebean.Finder;
import io.ebean.PagedList;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "agent")
public class Agent extends RootEntity {

  private String idValue;
  private String idSchemeID;
  private String name;
  @OneToOne(fetch = FetchType.LAZY)
  private Address address;
  private Date creationTime;

  @ManyToMany
  private List<Procedure> procedures;

  public Agent() {
  }

  public Agent(String idValue, String idSchemeID, String name, Address address) {
    this.idValue = idValue;
    this.idSchemeID = idSchemeID;
    this.name = name;
    this.address = address;
    this.creationTime = new Date();
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
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
    Agent agent = (Agent) o;
    return Objects.equals(idValue, agent.idValue) &&
        Objects.equals(idSchemeID, agent.idSchemeID) &&
        Objects.equals(name, agent.name) &&
        Objects.equals(address, agent.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idValue, idSchemeID, name, address);
  }

  public static Finder<Long, Agent> find = new Finder(Agent.class);


  public static Agent byId(Long id) {
    return find.byId(id);
  }


  public static List<Agent> all() {
    return find.all();
  }


  public static int maxPage(int pageSize) {
    int all = find.query().findCount();
    return (int) Math.ceil(all * 1.0 / pageSize);
  }

  public static List<Agent> getPage(int page, int pageSize) {
    PagedList<Agent> pagedList = find.query().orderBy("creationTime desc")
        .setFirstRow(page * pageSize)
        .setMaxRows(pageSize)
        .findPagedList();
    return pagedList.getList();
  }
}
