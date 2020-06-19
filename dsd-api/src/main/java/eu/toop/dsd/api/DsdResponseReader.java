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
package eu.toop.dsd.api;

import com.helger.pd.searchapi.v1.MatchType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.IJAXBVersatileReader;

import java.util.List;

/**
 * A class to read DSD responses
 *
 * @author yerlibilgin
 */
public class DsdResponseReader {

  /**
   * Create a reader that returns reads a {@link List} of {@link MatchType} objects from sources
   * @return the reader
   */
  public static IJAXBVersatileReader<List<MatchType>> matchTypeListReader() {
    return new MatchTypeListReader();
  }

  public static IJAXBVersatileReader<List<DCatAPDatasetType>> dcatDatasetTypeReader() {
    return new DcatDatasetTypeReader();
  }
}