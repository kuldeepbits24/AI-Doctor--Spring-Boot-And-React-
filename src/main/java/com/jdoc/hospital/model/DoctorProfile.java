package com.jdoc.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class DoctorProfile {

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

  @Column(length = 30)
  private String mobile;

  @Column(nullable = false, length = 80)
  private String department;

  @Column(nullable = false)
  private boolean approved = false;

  public DoctorProfile() {}

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

  public String getDepartment() { return department; }
  public void setDepartment(String department) { this.department = department; }

  public boolean isApproved() { return approved; }
  public void setApproved(boolean approved) { this.approved = approved; }
}
