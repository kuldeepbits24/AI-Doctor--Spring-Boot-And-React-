package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {
  Optional<DoctorProfile> findByUserId(Long userId);
  List<DoctorProfile> findByApproved(boolean approved);
  List<DoctorProfile> findByApprovedTrue();
  long countByApproved(boolean approved);
}
