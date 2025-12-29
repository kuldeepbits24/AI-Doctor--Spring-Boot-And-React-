package com.jdoc.hospital.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to serialize JSON", e);
    }
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse JSON", e);
    }
  }

  public static ObjectMapper mapper() {
    return mapper;
  }
}
