package com.jdoc.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "final_diagnosis")
public class FinalDiagnosis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "patient_id", nullable = false)
  private PatientProfile patient;

  @ManyToOne(optional = false)
  @JoinColumn(name = "doctor_id", nullable = false)
  private DoctorProfile doctor;

  @Lob
  private String symptomsJson;

  @Lob
  private String diseasesJson;

  @Lob
  private String medicinesJson;

  @Lob
  private String medicalTestsJson;

  @Column(nullable = false)
  private LocalDate createdDate = LocalDate.now();

  public FinalDiagnosis() {}

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public PatientProfile getPatient() { return patient; }
  public void setPatient(PatientProfile patient) { this.patient = patient; }

  public DoctorProfile getDoctor() { return doctor; }
  public void setDoctor(DoctorProfile doctor) { this.doctor = doctor; }

  public String getSymptomsJson() { return symptomsJson; }
  public void setSymptomsJson(String symptomsJson) { this.symptomsJson = symptomsJson; }

  public String getDiseasesJson() { return diseasesJson; }
  public void setDiseasesJson(String diseasesJson) { this.diseasesJson = diseasesJson; }

  public String getMedicinesJson() { return medicinesJson; }
  public void setMedicinesJson(String medicinesJson) { this.medicinesJson = medicinesJson; }

  public String getMedicalTestsJson() { return medicalTestsJson; }
  public void setMedicalTestsJson(String medicalTestsJson) { this.medicalTestsJson = medicalTestsJson; }

  public LocalDate getCreatedDate() { return createdDate; }
  public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
}
