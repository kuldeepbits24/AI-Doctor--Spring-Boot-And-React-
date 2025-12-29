package com.jdoc.hospital.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterAdminRequest {
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String username;
  @NotBlank
  private String password;

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}
