package com.jdoc.hospital.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DischargeRequest {
  @NotNull @Min(0)
  private Integer roomChargePerDay;
  @NotNull @Min(0)
  private Integer medicineCost;
  @NotNull @Min(0)
  private Integer doctorFee;
  @NotNull @Min(0)
  private Integer otherCharge;

  public Integer getRoomChargePerDay() { return roomChargePerDay; }
  public void setRoomChargePerDay(Integer roomChargePerDay) { this.roomChargePerDay = roomChargePerDay; }
  public Integer getMedicineCost() { return medicineCost; }
  public void setMedicineCost(Integer medicineCost) { this.medicineCost = medicineCost; }
  public Integer getDoctorFee() { return doctorFee; }
  public void setDoctorFee(Integer doctorFee) { this.doctorFee = doctorFee; }
  public Integer getOtherCharge() { return otherCharge; }
  public void setOtherCharge(Integer otherCharge) { this.otherCharge = otherCharge; }
}
