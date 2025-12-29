package com.jdoc.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient_discharge")
public class PatientDischargeDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long patientProfileId;

  @Column(nullable = false, length = 120)
  private String patientName;

  @Column(nullable = false, length = 120)
  private String assignedDoctorName;

  @Column(nullable = false, length = 120)
  private String address;

  @Column(length = 30)
  private String mobile;

  @Column(length = 200)
  private String symptoms;

  @Column(nullable = false)
  private LocalDate admitDate;

  @Column(nullable = false)
  private LocalDate releaseDate;

  @Column(nullable = false)
  private Integer daySpent;

  @Column(nullable = false)
  private Integer roomCharge;

  @Column(nullable = false)
  private Integer medicineCost;

  @Column(nullable = false)
  private Integer doctorFee;

  @Column(nullable = false)
  private Integer otherCharge;

  @Column(nullable = false)
  private Integer total;

  public PatientDischargeDetails() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getPatientProfileId() { return patientProfileId; }
  public void setPatientProfileId(Long patientProfileId) { this.patientProfileId = patientProfileId; }

  public String getPatientName() { return patientName; }
  public void setPatientName(String patientName) { this.patientName = patientName; }

  public String getAssignedDoctorName() { return assignedDoctorName; }
  public void setAssignedDoctorName(String assignedDoctorName) { this.assignedDoctorName = assignedDoctorName; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getMobile() { return mobile; }
  public void setMobile(String mobile) { this.mobile = mobile; }

  public String getSymptoms() { return symptoms; }
  public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

  public LocalDate getAdmitDate() { return admitDate; }
  public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }

  public LocalDate getReleaseDate() { return releaseDate; }
  public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

  public Integer getDaySpent() { return daySpent; }
  public void setDaySpent(Integer daySpent) { this.daySpent = daySpent; }

  public Integer getRoomCharge() { return roomCharge; }
  public void setRoomCharge(Integer roomCharge) { this.roomCharge = roomCharge; }

  public Integer getMedicineCost() { return medicineCost; }
  public void setMedicineCost(Integer medicineCost) { this.medicineCost = medicineCost; }

  public Integer getDoctorFee() { return doctorFee; }
  public void setDoctorFee(Integer doctorFee) { this.doctorFee = doctorFee; }

  public Integer getOtherCharge() { return otherCharge; }
  public void setOtherCharge(Integer otherCharge) { this.otherCharge = otherCharge; }

  public Integer getTotal() { return total; }
  public void setTotal(Integer total) { this.total = total; }
}
