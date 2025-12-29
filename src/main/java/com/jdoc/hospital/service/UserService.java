package com.jdoc.hospital.service;

import com.jdoc.hospital.dto.RegisterAdminRequest;
import com.jdoc.hospital.dto.RegisterDoctorRequest;
import com.jdoc.hospital.dto.RegisterPatientRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.AppUserRepository;
import com.jdoc.hospital.repo.DoctorRepository;
import com.jdoc.hospital.repo.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final AppUserRepository userRepo;
  private final DoctorRepository doctorRepo;
  private final PatientRepository patientRepo;
  private final PasswordEncoder passwordEncoder;

  public UserService(AppUserRepository userRepo, DoctorRepository doctorRepo, PatientRepository patientRepo,
                     PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.doctorRepo = doctorRepo;
    this.patientRepo = patientRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public AppUser registerAdmin(RegisterAdminRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }
    AppUser user = new AppUser(req.getUsername(), passwordEncoder.encode(req.getPassword()),
        req.getFirstName(), req.getLastName(), null, Role.ADMIN);
    return userRepo.save(user);
  }

  @Transactional
  public DoctorProfile registerDoctor(RegisterDoctorRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }
    AppUser user = new AppUser(req.getUsername(), passwordEncoder.encode(req.getPassword()),
        req.getFirstName(), req.getLastName(), null, Role.DOCTOR);
    user = userRepo.save(user);

    DoctorProfile doctor = new DoctorProfile();
    doctor.setUser(user);
    doctor.setAddress(req.getAddress());
    doctor.setMobile(req.getMobile());
    doctor.setDepartment(req.getDepartment());
    doctor.setApproved(false);
    return doctorRepo.save(doctor);
  }

  @Transactional
  public PatientProfile registerPatient(RegisterPatientRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }
    AppUser user = new AppUser(req.getUsername(), passwordEncoder.encode(req.getPassword()),
        req.getFirstName(), req.getLastName(), null, Role.PATIENT);
    user = userRepo.save(user);

    PatientProfile patient = new PatientProfile();
    patient.setUser(user);
    patient.setAddress(req.getAddress());
    patient.setMobile(req.getMobile());
    patient.setSymptoms(req.getSymptoms());
    patient.setAssignedDoctorUserId(req.getAssignedDoctorUserId());
    patient.setApproved(false);
    return patientRepo.save(patient);
  }
}
