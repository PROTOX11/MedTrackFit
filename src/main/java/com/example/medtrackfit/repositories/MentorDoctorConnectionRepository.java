package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.MentorDoctorConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorDoctorConnectionRepository extends JpaRepository<MentorDoctorConnection, Long> {
    
    List<MentorDoctorConnection> findByMentorId(String mentorId);
    
    List<MentorDoctorConnection> findByDoctorId(String doctorId);
    
    Optional<MentorDoctorConnection> findByMentorIdAndDoctorId(String mentorId, String doctorId);
    
    boolean existsByMentorIdAndDoctorId(String mentorId, String doctorId);
    
    void deleteByMentorIdAndDoctorId(String mentorId, String doctorId);
}
