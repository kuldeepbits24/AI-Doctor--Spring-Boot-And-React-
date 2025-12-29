package com.jdoc.hospital.controller;

import com.jdoc.hospital.dto.FinalDiagnosisRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.DoctorRepository;
import com.jdoc.hospital.repo.PatientRepository;
import com.jdoc.hospital.security.CustomUserDetails;
import com.jdoc.hospital.service.AppointmentService;
import com.jdoc.hospital.service.DiagnosisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

  private final DoctorRepository doctorRepo;
  private final PatientRepository patientRepo;
  private final AppointmentService appointmentService;
  private final DiagnosisService diagnosisService;

  public DoctorController(DoctorRepository doctorRepo, PatientRepository patientRepo,
                          AppointmentService appointmentService, DiagnosisService diagnosisService) {
    this.doctorRepo = doctorRepo;
    this.patientRepo = patientRepo;
    this.appointmentService = appointmentService;
    this.diagnosisService = diagnosisService;
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails principal) {
    DoctorProfile d = doctorRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));
    return ResponseEntity.ok(Map.of(
        "id", d.getId(),
        "userId", d.getUser().getId(),
        "name", d.getUser().getFullName(),
        "department", d.getDepartment(),
        "approved", d.isApproved()
    ));
  }

  @GetMapping("/patients")
  public ResponseEntity<?> patients(@AuthenticationPrincipal CustomUserDetails principal,
                                    @RequestParam(required = false) String q) {
    Long docUserId = principal.getUser().getId();

    List<PatientProfile> patients;
    if (q == null || q.isBlank()) {
      patients = patientRepo.findByApprovedTrueAndAssignedDoctorUserId(docUserId);
    } else {
      // simplest search: by symptoms
      patients = patientRepo.findByApprovedTrueAndAssignedDoctorUserIdAndSymptomsContainingIgnoreCase(docUserId, q);
    }

    var out = patients.stream().map(p -> Map.<String, Object>of(
        "id", p.getId(),
        "userId", p.getUser().getId(),
        "name", p.getUser().getFullName(),
        "mobile", p.getMobile(),
        "address", p.getAddress(),
        "symptoms", p.getSymptoms(),
        "admitDate", String.valueOf(p.getAdmitDate())
    )).collect(Collectors.toList());

    return ResponseEntity.ok(out);
  }

  @GetMapping("/appointments")
  public ResponseEntity<?> appointments(@AuthenticationPrincipal CustomUserDetails principal) {
    return ResponseEntity.ok(appointmentService.getForDoctor(principal.getUser().getId()));
  }

  @DeleteMapping("/appointments/{appointmentId}")
  public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
    appointmentService.deleteAppointment(appointmentId);
    return ResponseEntity.ok(Map.of("deleted", true));
  }

  @GetMapping("/patients/{patientId}/ai-diagnosis/latest")
  public ResponseEntity<?> latestAi(@PathVariable Long patientId) {
    Diagnosis d = diagnosisService.latestAiDiagnosisForPatient(patientId);
    return ResponseEntity.ok(d);
  }

  @PostMapping("/patients/{patientId}/final-diagnosis")
  public ResponseEntity<?> saveFinal(@AuthenticationPrincipal CustomUserDetails principal,
                                     @PathVariable Long patientId,
                                     @Valid @RequestBody FinalDiagnosisRequest req) {
    FinalDiagnosis fd = diagnosisService.saveFinalDiagnosis(patientId, principal.getUser().getId(), req);
    return ResponseEntity.ok(Map.of("finalDiagnosisId", fd.getId()));
  }

  @GetMapping("/patients/{patientId}/final-diagnosis/latest")
  public ResponseEntity<?> latestFinal(@PathVariable Long patientId) {
    FinalDiagnosis fd = diagnosisService.latestFinalDiagnosisForPatient(patientId);
    return ResponseEntity.ok(fd);
  }
}
