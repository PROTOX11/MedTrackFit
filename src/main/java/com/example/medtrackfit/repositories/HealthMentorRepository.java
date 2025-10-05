package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.HealthMentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthMentorRepository extends JpaRepository<HealthMentor, String> {
    Optional<HealthMentor> findByEmail(String email);
    boolean existsByEmail(String email);
}
