package com.jdoc.hospital.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jdoc.hospital.dto.ChatRequest;
import com.jdoc.hospital.dto.ChatResponse;
import com.jdoc.hospital.model.PatientProfile;
import com.jdoc.hospital.security.CustomUserDetails;
import com.jdoc.hospital.service.DiagnosisService;
import com.jdoc.hospital.service.OllamaService;
import com.jdoc.hospital.repo.PatientRepository;
import com.jdoc.hospital.util.JsonUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

  private final OllamaService ollamaService;
  private final PatientRepository patientRepo;
  private final DiagnosisService diagnosisService;

  public ChatController(OllamaService ollamaService, PatientRepository patientRepo, DiagnosisService diagnosisService) {
    this.ollamaService = ollamaService;
    this.patientRepo = patientRepo;
    this.diagnosisService = diagnosisService;
  }

  @PostMapping
  public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest req,
                                          @AuthenticationPrincipal CustomUserDetails principal) {
    String prompt = "You are a highly knowledgeable medical assistant. \n" +
        "Given a symptom described by the user, provide a detailed analysis including:\n" +
        "1. A list of possible diseases or conditions.\n" +
        "2. Suggested medicines or treatments.\n" +
        "3. Recommended medical tests.\n\n" +
        "Important: Always respond ONLY in valid JSON with this exact schema:\n" +
        "{\n" +
        "  \"symptoms\": [\"symptom1\", ...],\n" +
        "  \"diseases\": {\"disease1\": \"description\", ...},\n" +
        "  \"medicines\": {\"medicine1\": \"usage\", ...},\n" +
        "  \"medical_tests\": [\"test1\", ...]\n" +
        "}\n\n" +
        "Symptom: " + req.getUserInput();

    String raw = ollamaService.chatOnce(prompt);

    boolean recorded = false;
    JsonNode parsed = tryExtractJson(raw);

    if (parsed != null && principal != null && principal.getUser() != null
        && "PATIENT".equals(principal.getUser().getRole().name())) {
      PatientProfile patient = patientRepo.findByUserId(principal.getUser().getId()).orElse(null);
      if (patient != null) {
        diagnosisService.createFromAiJson(patient, parsed);
        recorded = true;
      }
    }

    return ResponseEntity.ok(new ChatResponse(raw, recorded));
  }

  private JsonNode tryExtractJson(String text) {
    if (text == null) return null;
    int start = text.indexOf('{');
    int end = text.lastIndexOf('}');
    if (start < 0 || end <= start) return null;
    String jsonStr = text.substring(start, end + 1);
    try {
      return JsonUtils.mapper().readTree(jsonStr);
    } catch (Exception e) {
      return null;
    }
  }
}
