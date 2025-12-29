package com.jdoc.hospital.controller;

import com.jdoc.hospital.dto.CreateAppointmentRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.*;
import com.jdoc.hospital.security.CustomUserDetails;
import com.jdoc.hospital.service.AppointmentService;
import com.jdoc.hospital.service.DiagnosisService;
import com.jdoc.hospital.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

  private final PatientRepository patientRepo;
  private final DoctorRepository doctorRepo;
  private final DischargeRepository dischargeRepo;
  private final AppointmentService appointmentService;
  private final DiagnosisService diagnosisService;
  private final PdfService pdfService;

  public PatientController(PatientRepository patientRepo, DoctorRepository doctorRepo, DischargeRepository dischargeRepo,
                           AppointmentService appointmentService, DiagnosisService diagnosisService, PdfService pdfService) {
    this.patientRepo = patientRepo;
    this.doctorRepo = doctorRepo;
    this.dischargeRepo = dischargeRepo;
    this.appointmentService = appointmentService;
    this.diagnosisService = diagnosisService;
    this.pdfService = pdfService;
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails principal) {
    PatientProfile p = patientRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));

    return ResponseEntity.ok(Map.of(
        "id", p.getId(),
        "userId", p.getUser().getId(),
        "name", p.getUser().getFullName(),
        "mobile", p.getMobile(),
        "address", p.getAddress(),
        "symptoms", p.getSymptoms(),
        "admitDate", String.valueOf(p.getAdmitDate()),
        "approved", p.isApproved(),
        "assignedDoctorUserId", p.getAssignedDoctorUserId()
    ));
  }

  @GetMapping("/appointments")
  public ResponseEntity<?> myAppointments(@AuthenticationPrincipal CustomUserDetails principal) {
    return ResponseEntity.ok(appointmentService.getForPatient(principal.getUser().getId()));
  }

  @PostMapping("/appointments")
  public ResponseEntity<?> createAppointment(@AuthenticationPrincipal CustomUserDetails principal,
                                             @Valid @RequestBody CreateAppointmentRequest req) {
    Appointment a = appointmentService.createForPatient(principal.getUser(), req);
    return ResponseEntity.ok(Map.of("appointmentId", a.getId(), "approved", a.isApproved()));
  }

  @GetMapping("/discharge/latest")
  public ResponseEntity<?> latestDischarge(@AuthenticationPrincipal CustomUserDetails principal) {
    PatientProfile p = patientRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
    var list = dischargeRepo.findByPatientProfileIdOrderByIdDesc(p.getId());
    return ResponseEntity.ok(list.isEmpty() ? null : list.get(0));
  }

  @GetMapping("/discharge/pdf")
  public ResponseEntity<byte[]> dischargePdf(@AuthenticationPrincipal CustomUserDetails principal) {
    PatientProfile p = patientRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
    var list = dischargeRepo.findByPatientProfileIdOrderByIdDesc(p.getId());
    if (list.isEmpty()) throw new IllegalArgumentException("No discharge bill found");
    byte[] pdf = pdfService.buildDischargePdf(list.get(0));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=discharge_bill_" + p.getId() + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }

  @GetMapping("/prescription/latest")
  public ResponseEntity<?> latestPrescription(@AuthenticationPrincipal CustomUserDetails principal) {
    PatientProfile p = patientRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
    FinalDiagnosis fd = diagnosisService.latestFinalDiagnosisForPatient(p.getId());
    return ResponseEntity.ok(fd);
  }

  @GetMapping("/prescription/pdf")
  public ResponseEntity<byte[]> prescriptionPdf(@AuthenticationPrincipal CustomUserDetails principal) {
    PatientProfile p = patientRepo.findByUserId(principal.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
    FinalDiagnosis fd = diagnosisService.latestFinalDiagnosisForPatient(p.getId());
    if (fd == null) throw new IllegalArgumentException("No prescription found");

    String docName = fd.getDoctor().getUser().getFullName();
    byte[] pdf = pdfService.buildPrescriptionPdf(p.getUser().getFullName(), p.getAddress(), p.getMobile(), docName, fd);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prescription_" + p.getId() + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }

  @GetMapping("/doctors")
  public ResponseEntity<?> doctors() {
    List<DoctorProfile> docs = doctorRepo.findByApprovedTrue();
    var out = docs.stream().map(d -> Map.<String, Object>of(
        "doctorId", d.getId(),
        "userId", d.getUser().getId(),
        "name", d.getUser().getFullName(),
        "department", d.getDepartment()
    )).toList();
    return ResponseEntity.ok(out);
  }
}
