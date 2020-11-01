package eu.toop.roa.model;


import io.ebean.Finder;
import io.ebean.PagedList;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "agent")
public class Agent extends RootEntity {

  private String id;
  private String idSchemeID;
  private String name;

  @OneToOne(fetch = FetchType.LAZY)
  private Address address;
  private Date creationTime;

  public Agent() {
  }

  public Agent(String id, String idSchemeID, String name, Address address) {
    this.id = id;
    this.idSchemeID = idSchemeID;
    this.name = name;
    this.address = address;
    this.creationTime = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
