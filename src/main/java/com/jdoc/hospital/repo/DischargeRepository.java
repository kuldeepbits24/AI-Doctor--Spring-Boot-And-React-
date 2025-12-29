package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.PatientDischargeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DischargeRepository extends JpaRepository<PatientDischargeDetails, Long> {
  List<PatientDischargeDetails> findByPatientProfileIdOrderByIdDesc(Long patientProfileId);
  List<PatientDischargeDetails> findByAssignedDoctorName(String assignedDoctorName);
}
