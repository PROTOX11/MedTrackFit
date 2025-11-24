package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.DoctorConnectedPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorConnectedPatientRepository extends JpaRepository<DoctorConnectedPatient, Long> {

    List<DoctorConnectedPatient> findByDoctorId(String doctorId);

    List<DoctorConnectedPatient> findByConnectedPatientId(String connectedPatientId);

    Optional<DoctorConnectedPatient> findByDoctorIdAndConnectedPatientId(String doctorId, String connectedPatientId);

    boolean existsByDoctorIdAndConnectedPatientId(String doctorId, String connectedPatientId);
}
