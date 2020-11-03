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
