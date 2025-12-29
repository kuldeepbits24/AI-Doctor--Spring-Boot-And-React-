package com.jdoc.hospital.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jdoc.hospital.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaService {

  private final HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();

  private final String baseUrl;
  private final String model;

  public OllamaService(@Value("${app.ollama.baseUrl}") String baseUrl,
                       @Value("${app.ollama.model}") String model) {
    this.baseUrl = baseUrl;
    this.model = model;
  }

  public String chatOnce(String userPrompt) {
    try {
      Map<String, Object> payload = new HashMap<>();
      payload.put("model", model);
      payload.put("stream", false);
      payload.put("messages", List.of(Map.of("role", "user", "content", userPrompt)));

      String body = JsonUtils.toJson(payload);
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/api/chat"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
      if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
        JsonNode root = JsonUtils.mapper().readTree(resp.body());
        JsonNode msg = root.get("message");
        if (msg != null && msg.get("content") != null) {
          return msg.get("content").asText();
        }
        // Some Ollama versions may return a slightly different payload
        return resp.body();
      }
      return "Ollama error (HTTP " + resp.statusCode() + ")";
    } catch (Exception e) {
      return "Ollama is not reachable. Start Ollama on http://localhost:11434 and ensure the model '" + model + "' exists.";
    }
  }
}
