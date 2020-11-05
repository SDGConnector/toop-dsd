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
package eu.toop.roa.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
  }

  public static String toJson(Object object) {
    synchronized (objectMapper) {
      try {
        return objectMapper.writeValueAsString(object);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        throw new IllegalArgumentException("Couldn't generate JSON", e);
      }
    }
  }

  public static <T> T readValue(String jsonContent, Class<T> aClass) throws JsonProcessingException {
    return objectMapper.readValue(jsonContent, aClass);
  }

  public static <T> T readValue(JsonParser jsonParser, Class<T> aClass) throws IOException {
    return objectMapper.readValue(jsonParser, aClass);

  }
}
