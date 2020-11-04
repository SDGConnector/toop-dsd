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