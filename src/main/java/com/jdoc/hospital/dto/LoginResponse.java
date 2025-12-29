package com.jdoc.hospital.dto;

public class LoginResponse {
  private String token;
  private String role;
  private Long userId;
  private String fullName;

  public LoginResponse() {}

  public LoginResponse(String token, String role, Long userId, String fullName) {
    this.token = token;
    this.role = role;
    this.userId = userId;
    this.fullName = fullName;
  }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
}
