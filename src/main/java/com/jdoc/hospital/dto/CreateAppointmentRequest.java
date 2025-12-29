package com.jdoc.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAppointmentRequest {
  @NotNull
  private Long doctorUserId;
  @NotBlank
  private String description;

  public Long getDoctorUserId() { return doctorUserId; }
  public void setDoctorUserId(Long doctorUserId) { this.doctorUserId = doctorUserId; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}
