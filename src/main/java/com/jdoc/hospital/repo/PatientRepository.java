package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientProfile, Long> {
  Optional<PatientProfile> findByUserId(Long userId);
  List<PatientProfile> findByApproved(boolean approved);
  long countByApproved(boolean approved);

  List<PatientProfile> findByApprovedTrueAndAssignedDoctorUserId(Long assignedDoctorUserId);
  List<PatientProfile> findByApprovedTrueAndAssignedDoctorUserIdAndUserFirstNameContainingIgnoreCase(Long assignedDoctorUserId, String firstNamePart);
}
