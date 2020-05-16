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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * An interface that declares the methods for serializing data
 *
 * @author yerlibilgin
 */
public interface IReader<RESULTTYPE> {

  /**
   * An abstract method for reading the contents of the given {@link Reader} as <code>RESULTTYPE</code>
   * @param reader the reader instance
   */
  RESULTTYPE read(Reader reader);

  /**
   * An abstract method for reading the contents of the given {@link InputStream} as <code>RESULTTYPE</code>
   * @param inputStream the input stream instance
   */
  RESULTTYPE read(InputStream inputStream);

  /**
   * An abstract method for reading the contents of the given {@link String} as <code>RESULTTYPE</code>
   * @param contents the String instance
   */
  RESULTTYPE fromString(String contents);
}
