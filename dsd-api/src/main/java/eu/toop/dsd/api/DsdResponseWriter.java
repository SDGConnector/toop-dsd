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
package eu.toop.dsd.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class to write DSD responses
 *
 * @author yerlibilgin
 */
public class DsdResponseWriter {

  /**
   * Converts a DIR result to a DSD result
   *
   * @param directoryResult the xml received from the toop directory
   * @return
   */
  public static String convertDIRToDSD(@Nonnull String directoryResult) {
    return convertDIRToDSD(directoryResult, null);
  }

  /**
   * Converts a DIR result to a DSD result
   *
   * @param directoryResult the xml received from the toop directory
   * @param datasetType     the optional datasetType parameter, used for filtering the record
   * @return
   */
  public static String convertDIRToDSD(@Nonnull String directoryResult, @Nullable String datasetType) {
    return null;
  }
}
