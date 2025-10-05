package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.SufferingPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SufferingPatientRepository extends JpaRepository<SufferingPatient, String> {
    Optional<SufferingPatient> findByEmail(String email);
    boolean existsByEmail(String email);
}
