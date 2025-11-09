package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.DoctorPatientConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface DoctorPatientConnectionRepository extends JpaRepository<DoctorPatientConnection, String> {

    // Find connection by doctor ID
    Optional<DoctorPatientConnection> findByDoctorId(String doctorId);

    // Check if connection exists for doctor
    boolean existsByDoctorId(String doctorId);

    // Check if patient is connected to doctor
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM DoctorPatientConnection c WHERE c.doctorId = :doctorId AND :patientId MEMBER OF c.patientIds")
    boolean existsByDoctorIdAndPatientId(@Param("doctorId") String doctorId, @Param("patientId") String patientId);

    // Find all connections
    List<DoctorPatientConnection> findAll();
}
