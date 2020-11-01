package eu.toop.roa.model;

import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public class RootEntity extends Model {

  @Id
  public long id;

  public static List<Long> getIdList(List<? extends RootEntity> all) {
    List<Long> ids = new ArrayList<>(all.size());
    for (RootEntity u : all) {
      ids.add(u.id);
    }
    return ids;
  }
}