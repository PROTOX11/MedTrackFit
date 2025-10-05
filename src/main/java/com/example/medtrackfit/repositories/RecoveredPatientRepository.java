package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.RecoveredPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoveredPatientRepository extends JpaRepository<RecoveredPatient, String> {
    Optional<RecoveredPatient> findByEmail(String email);
    boolean existsByEmail(String email);
}
