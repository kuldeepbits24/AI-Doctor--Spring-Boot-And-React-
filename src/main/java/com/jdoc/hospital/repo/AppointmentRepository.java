package com.jdoc.hospital.repo;

import com.jdoc.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  List<Appointment> findByApproved(boolean approved);
  long countByApproved(boolean approved);

  List<Appointment> findByApprovedTrueAndDoctorUserId(Long doctorUserId);
  List<Appointment> findByPatientUserId(Long patientUserId);
}
