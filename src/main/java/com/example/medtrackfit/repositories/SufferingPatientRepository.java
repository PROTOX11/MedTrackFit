package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.SufferingPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SufferingPatientRepository extends JpaRepository<SufferingPatient, String> {
    Optional<SufferingPatient> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT sp FROM SufferingPatient sp WHERE LOWER(sp.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SufferingPatient> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT sp FROM SufferingPatient sp WHERE LOWER(sp.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<SufferingPatient> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT sp FROM SufferingPatient sp WHERE sp.patientId = :patientId")
    Optional<SufferingPatient> findByPatientId(@Param("patientId") String patientId);

    List<SufferingPatient> findByDoctorId(String doctorId);
}
