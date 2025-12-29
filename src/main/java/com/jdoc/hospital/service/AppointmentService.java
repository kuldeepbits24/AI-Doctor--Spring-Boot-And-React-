package com.jdoc.hospital.service;

import com.jdoc.hospital.dto.CreateAppointmentRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepo;
  private final AppUserRepository userRepo;
  private final DoctorRepository doctorRepo;

  public AppointmentService(AppointmentRepository appointmentRepo, AppUserRepository userRepo, DoctorRepository doctorRepo) {
    this.appointmentRepo = appointmentRepo;
    this.userRepo = userRepo;
    this.doctorRepo = doctorRepo;
  }

  @Transactional
  public Appointment createForPatient(AppUser patientUser, CreateAppointmentRequest req) {
    AppUser doctorUser = userRepo.findById(req.getDoctorUserId())
        .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

    // Ensure doctor is approved
    doctorRepo.findByUserId(doctorUser.getId())
        .filter(DoctorProfile::isApproved)
        .orElseThrow(() -> new IllegalArgumentException("Doctor not approved"));

    Appointment a = new Appointment();
    a.setDoctorUserId(doctorUser.getId());
    a.setPatientUserId(patientUser.getId());
    a.setDoctorName(doctorUser.getFirstName());
    a.setPatientName(patientUser.getFirstName());
    a.setDescription(req.getDescription());
    a.setApproved(false);
    return appointmentRepo.save(a);
  }

  public List<Appointment> getForDoctor(Long doctorUserId) {
    return appointmentRepo.findByApprovedTrueAndDoctorUserId(doctorUserId);
  }

  public List<Appointment> getForPatient(Long patientUserId) {
    return appointmentRepo.findByPatientUserId(patientUserId);
  }

  @Transactional
  public void deleteAppointment(Long appointmentId) {
    appointmentRepo.deleteById(appointmentId);
  }
}
