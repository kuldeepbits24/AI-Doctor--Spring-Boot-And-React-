package com.jdoc.hospital.service;

import com.jdoc.hospital.dto.DischargeRequest;
import com.jdoc.hospital.model.*;
import com.jdoc.hospital.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

  private final DoctorRepository doctorRepo;
  private final PatientRepository patientRepo;
  private final AppointmentRepository appointmentRepo;
  private final DischargeRepository dischargeRepo;
  private final AppUserRepository userRepo;

  public AdminService(DoctorRepository doctorRepo, PatientRepository patientRepo,
                      AppointmentRepository appointmentRepo, DischargeRepository dischargeRepo,
                      AppUserRepository userRepo) {
    this.doctorRepo = doctorRepo;
    this.patientRepo = patientRepo;
    this.appointmentRepo = appointmentRepo;
    this.dischargeRepo = dischargeRepo;
    this.userRepo = userRepo;
  }

  public Map<String, Long> getCounts() {
    Map<String, Long> m = new HashMap<>();
    m.put("doctorApproved", (long) doctorRepo.findByApproved(true).size());
    m.put("doctorPending", (long) doctorRepo.findByApproved(false).size());
    m.put("patientApproved", (long) patientRepo.findByApproved(true).size());
    m.put("patientPending", (long) patientRepo.findByApproved(false).size());
    m.put("appointmentApproved", (long) appointmentRepo.findByApproved(true).size());
    m.put("appointmentPending", (long) appointmentRepo.findByApproved(false).size());
    return m;
  }

  public List<DoctorProfile> getDoctors(boolean approved) {
    return doctorRepo.findByApproved(approved);
  }

  public List<PatientProfile> getPatients(boolean approved) {
    return patientRepo.findByApproved(approved);
  }

  public List<Appointment> getAppointments(boolean approved) {
    return appointmentRepo.findByApproved(approved);
  }

  @Transactional
  public DoctorProfile setDoctorApproval(Long doctorId, boolean approved) {
    DoctorProfile d = doctorRepo.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    d.setApproved(approved);
    return doctorRepo.save(d);
  }

  @Transactional
  public PatientProfile setPatientApproval(Long patientId, boolean approved) {
    PatientProfile p = patientRepo.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    p.setApproved(approved);
    return patientRepo.save(p);
  }

  @Transactional
  public Appointment setAppointmentApproval(Long appointmentId, boolean approved) {
    Appointment a = appointmentRepo.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    a.setApproved(approved);
    return appointmentRepo.save(a);
  }

  @Transactional
  public void deleteDoctor(Long doctorId) {
    DoctorProfile d = doctorRepo.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    AppUser u = d.getUser();
    doctorRepo.delete(d);
    userRepo.delete(u);
  }

  @Transactional
  public void deletePatient(Long patientId) {
    PatientProfile p = patientRepo.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    AppUser u = p.getUser();
    patientRepo.delete(p);
    userRepo.delete(u);
  }

  @Transactional
  public void deleteAppointment(Long appointmentId) {
    appointmentRepo.deleteById(appointmentId);
  }

  @Transactional
  public PatientDischargeDetails dischargePatient(Long patientProfileId, DischargeRequest req) {
    PatientProfile p = patientRepo.findById(patientProfileId)
        .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

    AppUser assignedDoctor = null;
    if (p.getAssignedDoctorUserId() != null) {
      assignedDoctor = userRepo.findById(p.getAssignedDoctorUserId()).orElse(null);
    }

    LocalDate releaseDate = LocalDate.now();
    long days = Math.max(1, ChronoUnit.DAYS.between(p.getAdmitDate(), releaseDate));

    int roomCharge = req.getRoomChargePerDay() * (int) days;
    int total = roomCharge + req.getDoctorFee() + req.getMedicineCost() + req.getOtherCharge();

    PatientDischargeDetails dd = new PatientDischargeDetails();
    dd.setPatientProfileId(p.getId());
    dd.setPatientName(p.getUser().getFullName());
    dd.setAssignedDoctorName(assignedDoctor == null ? "" : assignedDoctor.getFirstName());
    dd.setAddress(p.getAddress());
    dd.setMobile(p.getMobile());
    dd.setSymptoms(p.getSymptoms());
    dd.setAdmitDate(p.getAdmitDate());
    dd.setReleaseDate(releaseDate);
    dd.setDaySpent((int) days);
    dd.setRoomCharge(roomCharge);
    dd.setMedicineCost(req.getMedicineCost());
    dd.setDoctorFee(req.getDoctorFee());
    dd.setOtherCharge(req.getOtherCharge());
    dd.setTotal(total);

    return dischargeRepo.save(dd);
  }
}
