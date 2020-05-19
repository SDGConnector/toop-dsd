/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.dsd.client;

import java.util.List;

import com.helger.pd.searchapi.v1.MatchType;

import eu.toop.edm.xml.IJAXBVersatileWriter;
import eu.toop.regrep.query.QueryResponse;

/**
 * A class to write DSD responses
 *
 * @author yerlibilgin
 */
public class DsdResponseWriter {

  /**
   * Create a DSD Response Writer that writes dataset type and MatchTypes (received from TOOP Directory)
   * as a {@link QueryResponse} XML.
   * @param s_DataSetType
   * @param matchTypes
   * @return
   */
  public static IJAXBVersatileWriter<QueryResponse> matchTypesWriter(String s_DataSetType, List<MatchType> matchTypes) {
    return new MatchTypesWriter(s_DataSetType, matchTypes);
  }


}
