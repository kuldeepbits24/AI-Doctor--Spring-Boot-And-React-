package com.jdoc.hospital.controller;

import com.jdoc.hospital.dto.*;
import com.jdoc.hospital.model.AppUser;
import com.jdoc.hospital.security.CustomUserDetails;
import com.jdoc.hospital.security.JwtTokenService;
import com.jdoc.hospital.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenService = jwtTokenService;
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    String token = jwtTokenService.generateToken(userDetails);
    AppUser u = userDetails.getUser();
    return ResponseEntity.ok(new LoginResponse(token, u.getRole().name(), u.getId(), u.getFullName()));
  }

  @PostMapping("/register/admin")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterAdminRequest req) {
    AppUser u = userService.registerAdmin(req);
    return ResponseEntity.ok().body(java.util.Map.of("id", u.getId(), "username", u.getUsername()));
  }

  @PostMapping("/register/doctor")
  public ResponseEntity<?> registerDoctor(@Valid @RequestBody RegisterDoctorRequest req) {
    var d = userService.registerDoctor(req);
    return ResponseEntity.ok().body(java.util.Map.of("doctorId", d.getId(), "approved", d.isApproved()));
  }

  @PostMapping("/register/patient")
  public ResponseEntity<?> registerPatient(@Valid @RequestBody RegisterPatientRequest req) {
    var p = userService.registerPatient(req);
    return ResponseEntity.ok().body(java.util.Map.of("patientId", p.getId(), "approved", p.isApproved()));
  }
}
