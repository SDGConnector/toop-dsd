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
package eu.toop.dsd.commons;

import java.io.OutputStream;
import java.io.Writer;

/**
 * An interface that declares the methods for serializing data
 */
public interface IWriter {

  /**
   * An abstract method for writing the contents to a {@link Writer} object
   * @param writer
   */
  void write(Writer writer);

  /**
   * An abstract method for writing the contents to a {@link OutputStream} object
   * @param outputStream
   */
  void write(OutputStream outputStream);

  /**
   * An abstract method for returning the contents as a {@link String}
   * @return the contents as a String
   */
  String getAsString();
}
