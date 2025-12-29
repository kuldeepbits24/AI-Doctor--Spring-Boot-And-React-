package com.jdoc.hospital.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class FinalDiagnosisRequest {
  @NotNull
  private List<String> symptoms;
  @NotNull
  private Map<String, String> diseases;
  @NotNull
  private Map<String, String> medicines;
  @NotNull
  private List<String> medicalTests;

  public List<String> getSymptoms() { return symptoms; }
  public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }
  public Map<String, String> getDiseases() { return diseases; }
  public void setDiseases(Map<String, String> diseases) { this.diseases = diseases; }
  public Map<String, String> getMedicines() { return medicines; }
  public void setMedicines(Map<String, String> medicines) { this.medicines = medicines; }
  public List<String> getMedicalTests() { return medicalTests; }
  public void setMedicalTests(List<String> medicalTests) { this.medicalTests = medicalTests; }
}
