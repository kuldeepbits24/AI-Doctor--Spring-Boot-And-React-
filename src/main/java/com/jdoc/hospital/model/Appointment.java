package com.jdoc.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long patientUserId;
  private Long doctorUserId;

  @Column(length = 120)
  private String patientName;

  @Column(length = 120)
  private String doctorName;

  @Column(nullable = false)
  private LocalDate appointmentDate = LocalDate.now();

  @Column(nullable = false, length = 500)
  private String description;

  @Column(nullable = false)
  private boolean approved = false;

  public Appointment() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getPatientUserId() { return patientUserId; }
  public void setPatientUserId(Long patientUserId) { this.patientUserId = patientUserId; }

  public Long getDoctorUserId() { return doctorUserId; }
  public void setDoctorUserId(Long doctorUserId) { this.doctorUserId = doctorUserId; }

  public String getPatientName() { return patientName; }
  public void setPatientName(String patientName) { this.patientName = patientName; }

  public String getDoctorName() { return doctorName; }
  public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

  public LocalDate getAppointmentDate() { return appointmentDate; }
  public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public boolean isApproved() { return approved; }
  public void setApproved(boolean approved) { this.approved = approved; }
}
