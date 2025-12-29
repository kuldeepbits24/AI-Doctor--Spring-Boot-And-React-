package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.FinalDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinalDiagnosisRepository extends JpaRepository<FinalDiagnosis, Long> {
  List<FinalDiagnosis> findByPatientIdOrderByIdDesc(Long patientId);
}
