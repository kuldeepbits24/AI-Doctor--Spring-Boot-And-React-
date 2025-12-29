package com.jdoc.hospital.controller;

import com.jdoc.hospital.model.DoctorProfile;
import com.jdoc.hospital.repo.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicController {

  private final DoctorRepository doctorRepo;

  public PublicController(DoctorRepository doctorRepo) {
    this.doctorRepo = doctorRepo;
  }

  @GetMapping("/departments")
  public ResponseEntity<List<String>> departments() {
    List<String> deps = List.of("Cardiologist", "Dermatologists", "Emergency Medicine Specialists",
        "Allergists/Immunologists", "Anesthesiologists", "Colon and Rectal Surgeons");
    return ResponseEntity.ok(deps);
  }

  @GetMapping("/doctors")
  public ResponseEntity<List<Map<String, Object>>> approvedDoctors() {
    List<DoctorProfile> docs = doctorRepo.findByApprovedTrue();
    var out = docs.stream().map(d -> Map.<String, Object>of(
        "doctorId", d.getId(),
        "userId", d.getUser().getId(),
        "name", d.getUser().getFullName(),
        "department", d.getDepartment()
    )).collect(Collectors.toList());
    return ResponseEntity.ok(out);
  }
}
