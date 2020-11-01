package eu.toop.roa.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "procedure")
public class Procedure extends RootEntity {
  private String value;

  @ManyToMany
  private List<Agent> agents;

  public Procedure() {
  }

  public Procedure(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
