package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.RecoveredPatientDoctorConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveredPatientDoctorConnectionRepository extends JpaRepository<RecoveredPatientDoctorConnection, Long> {
    
    List<RecoveredPatientDoctorConnection> findByRecoveredPatientId(String recoveredPatientId);
    
    List<RecoveredPatientDoctorConnection> findByDoctorId(String doctorId);
    
    Optional<RecoveredPatientDoctorConnection> findByRecoveredPatientIdAndDoctorId(String recoveredPatientId, String doctorId);
    
    boolean existsByRecoveredPatientIdAndDoctorId(String recoveredPatientId, String doctorId);
    
    void deleteByRecoveredPatientIdAndDoctorId(String recoveredPatientId, String doctorId);
}
