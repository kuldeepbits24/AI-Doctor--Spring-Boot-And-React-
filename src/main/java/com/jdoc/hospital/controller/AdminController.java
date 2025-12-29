package com.jdoc.hospital.controller;

import com.jdoc.hospital.dto.DischargeRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.DischargeRepository;
import com.jdoc.hospital.service.AdminService;
import com.jdoc.hospital.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminService adminService;
  private final DischargeRepository dischargeRepo;
  private final PdfService pdfService;

  public AdminController(AdminService adminService, DischargeRepository dischargeRepo, PdfService pdfService) {
    this.adminService = adminService;
    this.dischargeRepo = dischargeRepo;
    this.pdfService = pdfService;
  }

  @GetMapping("/counts")
  public ResponseEntity<Map<String, Long>> counts() {
    return ResponseEntity.ok(adminService.getCounts());
  }

  @GetMapping("/doctors")
  public ResponseEntity<List<Map<String, Object>>> doctors(@RequestParam(defaultValue = "true") boolean approved) {
    return ResponseEntity.ok(adminService.getDoctors(approved).stream().map(d -> Map.<String, Object>of(
        "id", d.getId(),
        "userId", d.getUser().getId(),
        "name", d.getUser().getFullName(),
        "department", d.getDepartment(),
        "mobile", d.getMobile(),
        "address", d.getAddress(),
        "approved", d.isApproved()
    )).collect(Collectors.toList()));
  }

  @PutMapping("/doctors/{doctorId}/approve")
  public ResponseEntity<?> approveDoctor(@PathVariable Long doctorId) {
    DoctorProfile d = adminService.setDoctorApproval(doctorId, true);
    return ResponseEntity.ok(Map.of("id", d.getId(), "approved", d.isApproved()));
  }

  @PutMapping("/doctors/{doctorId}/reject")
  public ResponseEntity<?> rejectDoctor(@PathVariable Long doctorId) {
    DoctorProfile d = adminService.setDoctorApproval(doctorId, false);
    return ResponseEntity.ok(Map.of("id", d.getId(), "approved", d.isApproved()));
  }

  @DeleteMapping("/doctors/{doctorId}")
  public ResponseEntity<?> deleteDoctor(@PathVariable Long doctorId) {
    adminService.deleteDoctor(doctorId);
    return ResponseEntity.ok(Map.of("deleted", true));
  }

  @GetMapping("/patients")
  public ResponseEntity<List<Map<String, Object>>> patients(@RequestParam(defaultValue = "true") boolean approved) {
    return ResponseEntity.ok(adminService.getPatients(approved).stream().map(p -> Map.<String, Object>of(
        "id", p.getId(),
        "userId", p.getUser().getId(),
        "name", p.getUser().getFullName(),
        "mobile", p.getMobile(),
        "address", p.getAddress(),
        "symptoms", p.getSymptoms(),
        "admitDate", String.valueOf(p.getAdmitDate()),
        "assignedDoctorUserId", p.getAssignedDoctorUserId(),
        "approved", p.isApproved()
    )).collect(Collectors.toList()));
  }

  @PutMapping("/patients/{patientId}/approve")
  public ResponseEntity<?> approvePatient(@PathVariable Long patientId) {
    PatientProfile p = adminService.setPatientApproval(patientId, true);
    return ResponseEntity.ok(Map.of("id", p.getId(), "approved", p.isApproved()));
  }

  @PutMapping("/patients/{patientId}/reject")
  public ResponseEntity<?> rejectPatient(@PathVariable Long patientId) {
    PatientProfile p = adminService.setPatientApproval(patientId, false);
    return ResponseEntity.ok(Map.of("id", p.getId(), "approved", p.isApproved()));
  }

  @DeleteMapping("/patients/{patientId}")
  public ResponseEntity<?> deletePatient(@PathVariable Long patientId) {
    adminService.deletePatient(patientId);
    return ResponseEntity.ok(Map.of("deleted", true));
  }

  @GetMapping("/appointments")
  public ResponseEntity<List<Appointment>> appointments(@RequestParam(defaultValue = "true") boolean approved) {
    return ResponseEntity.ok(adminService.getAppointments(approved));
  }

  @PutMapping("/appointments/{appointmentId}/approve")
  public ResponseEntity<?> approveAppointment(@PathVariable Long appointmentId) {
    Appointment a = adminService.setAppointmentApproval(appointmentId, true);
    return ResponseEntity.ok(Map.of("id", a.getId(), "approved", a.isApproved()));
  }

  @PutMapping("/appointments/{appointmentId}/reject")
  public ResponseEntity<?> rejectAppointment(@PathVariable Long appointmentId) {
    Appointment a = adminService.setAppointmentApproval(appointmentId, false);
    return ResponseEntity.ok(Map.of("id", a.getId(), "approved", a.isApproved()));
  }

  @DeleteMapping("/appointments/{appointmentId}")
  public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
    adminService.deleteAppointment(appointmentId);
    return ResponseEntity.ok(Map.of("deleted", true));
  }

  @PostMapping("/patients/{patientId}/discharge")
  public ResponseEntity<?> discharge(@PathVariable Long patientId, @Valid @RequestBody DischargeRequest req) {
    PatientDischargeDetails dd = adminService.dischargePatient(patientId, req);
    return ResponseEntity.ok(Map.of("dischargeId", dd.getId(), "total", dd.getTotal()));
  }

  @GetMapping("/patients/{patientId}/discharge/latest")
  public ResponseEntity<?> latestDischarge(@PathVariable Long patientId) {
    var list = dischargeRepo.findByPatientProfileIdOrderByIdDesc(patientId);
    return ResponseEntity.ok(list.isEmpty() ? null : list.get(0));
  }

  @GetMapping("/patients/{patientId}/discharge/pdf")
  public ResponseEntity<byte[]> dischargePdf(@PathVariable Long patientId) {
    var list = dischargeRepo.findByPatientProfileIdOrderByIdDesc(patientId);
    if (list.isEmpty()) throw new IllegalArgumentException("No discharge bill found");
    byte[] pdf = pdfService.buildDischargePdf(list.get(0));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=discharge_bill_" + patientId + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }
}
