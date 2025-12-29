package com.jdoc.hospital.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jdoc.hospital.dto.FinalDiagnosisRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.*;
import com.jdoc.hospital.util.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiagnosisService {

  private final DiagnosisRepository diagnosisRepo;
  private final FinalDiagnosisRepository finalDiagnosisRepo;
  private final PatientRepository patientRepo;
  private final DoctorRepository doctorRepo;

  public DiagnosisService(DiagnosisRepository diagnosisRepo, FinalDiagnosisRepository finalDiagnosisRepo,
                          PatientRepository patientRepo, DoctorRepository doctorRepo) {
    this.diagnosisRepo = diagnosisRepo;
    this.finalDiagnosisRepo = finalDiagnosisRepo;
    this.patientRepo = patientRepo;
    this.doctorRepo = doctorRepo;
  }

  @Transactional
  public Diagnosis createFromAiJson(PatientProfile patient, JsonNode json) {
    Diagnosis d = new Diagnosis();
    d.setPatient(patient);

    // Tries to support the JSON format used by the Django/Ollama workflow.
    // Expected keys: symptoms, diseases, medicines, medical_tests (but any missing key is allowed).
    JsonNode symptoms = json.get("symptoms");
    JsonNode diseases = json.get("diseases");
    JsonNode medicines = json.get("medicines");
    JsonNode medicalTests = json.get("medical_tests");

    if (symptoms != null) d.setSymptomsJson(symptoms.toString());
    if (diseases != null) d.setDiseasesJson(diseases.toString());
    if (medicines != null) d.setMedicinesJson(medicines.toString());
    if (medicalTests != null) d.setMedicalTestsJson(medicalTests.toString());

    return diagnosisRepo.save(d);
  }

  public Diagnosis latestAiDiagnosisForPatient(Long patientProfileId) {
    List<Diagnosis> all = diagnosisRepo.findByPatientIdOrderByDiagnosisDateDesc(patientProfileId);
    return all.isEmpty() ? null : all.get(0);
  }

  @Transactional
  public FinalDiagnosis saveFinalDiagnosis(Long patientProfileId, Long doctorUserId, FinalDiagnosisRequest req) {
    PatientProfile patient = patientRepo.findById(patientProfileId)
        .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

    DoctorProfile doctor = doctorRepo.findByUserId(doctorUserId)
        .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

    FinalDiagnosis fd = new FinalDiagnosis();
    fd.setPatient(patient);
    fd.setDoctor(doctor);
    fd.setSymptomsJson(JsonUtils.toJson(req.getSymptoms()));
    fd.setDiseasesJson(JsonUtils.toJson(req.getDiseases()));
    fd.setMedicinesJson(JsonUtils.toJson(req.getMedicines()));
    fd.setMedicalTestsJson(JsonUtils.toJson(req.getMedicalTests()));
    return finalDiagnosisRepo.save(fd);
  }

  public FinalDiagnosis latestFinalDiagnosisForPatient(Long patientProfileId) {
    List<FinalDiagnosis> all = finalDiagnosisRepo.findByPatientIdOrderByIdDesc(patientProfileId);
    return all.isEmpty() ? null : all.get(0);
  }
}
