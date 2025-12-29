package com.jdoc.hospital.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterDoctorRequest {
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String username;
  @NotBlank
  private String password;

  @NotBlank
  private String address;
  private String mobile;
  @NotBlank
  private String department;

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getMobile() { return mobile; }
  public void setMobile(String mobile) { this.mobile = mobile; }
  public String getDepartment() { return department; }
  public void setDepartment(String department) { this.department = department; }
}
