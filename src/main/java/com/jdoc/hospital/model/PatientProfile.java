package com.jdoc.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class PatientProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private AppUser user;

  @Column(length = 255)
  private String profilePicPath;

  @Column(nullable = false, length = 120)
  private String address;

  @Column(nullable = false, length = 30)
  private String mobile;

  @Column(nullable = false, length = 200)
  private String symptoms;

  /** Refers to the doctor's USER id (mirrors the Django field assignedDoctorId). */
  private Long assignedDoctorUserId;

  @Column(nullable = false)
  private LocalDate admitDate = LocalDate.now();

  @Column(nullable = false)
  private boolean approved = false;

  public PatientProfile() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public AppUser getUser() { return user; }
  public void setUser(AppUser user) { this.user = user; }

  public String getProfilePicPath() { return profilePicPath; }
  public void setProfilePicPath(String profilePicPath) { this.profilePicPath = profilePicPath; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getMobile() { return mobile; }
  public void setMobile(String mobile) { this.mobile = mobile; }

  public String getSymptoms() { return symptoms; }
  public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

  public Long getAssignedDoctorUserId() { return assignedDoctorUserId; }
  public void setAssignedDoctorUserId(Long assignedDoctorUserId) { this.assignedDoctorUserId = assignedDoctorUserId; }

  public LocalDate getAdmitDate() { return admitDate; }
  public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }

  public boolean isApproved() { return approved; }
  public void setApproved(boolean approved) { this.approved = approved; }
}
