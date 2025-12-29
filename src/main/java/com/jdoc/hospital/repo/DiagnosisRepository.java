package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
  List<Diagnosis> findByPatientIdOrderByDiagnosisDateDesc(Long patientId);
}
