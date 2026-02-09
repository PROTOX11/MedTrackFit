package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.RecoveredPatientMentorConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveredPatientMentorConnectionRepository extends JpaRepository<RecoveredPatientMentorConnection, Long> {
    
    List<RecoveredPatientMentorConnection> findByRecoveredPatientId(String recoveredPatientId);
    
    List<RecoveredPatientMentorConnection> findByMentorId(String mentorId);
    
    Optional<RecoveredPatientMentorConnection> findByRecoveredPatientIdAndMentorId(String recoveredPatientId, String mentorId);
    
    boolean existsByRecoveredPatientIdAndMentorId(String recoveredPatientId, String mentorId);
    
    void deleteByRecoveredPatientIdAndMentorId(String recoveredPatientId, String mentorId);
}
