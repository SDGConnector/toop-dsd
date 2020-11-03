package eu.toop.roa.model;

import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Root entity. An ancestor to all ebean model entities
 * Provides a default database id (long)
 */
@MappedSuperclass
public class RootEntity extends Model {

  /**
   * The Id.
   */
  @Id
  public long id;

  /**
   * Gets id list.
   *
   * @param all the all
   * @return the id list
   */
  public static List<Long> getIdList(List<? extends RootEntity> all) {
    List<Long> ids = new ArrayList<>(all.size());
    for (RootEntity u : all) {
      ids.add(u.id);
    }
    return ids;
  }
}