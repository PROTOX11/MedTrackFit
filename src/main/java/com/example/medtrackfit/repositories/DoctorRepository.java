package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);
    boolean existsByEmail(String email);
}
