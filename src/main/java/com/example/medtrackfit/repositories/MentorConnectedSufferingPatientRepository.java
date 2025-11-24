package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.MentorConnectedSufferingPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorConnectedSufferingPatientRepository extends JpaRepository<MentorConnectedSufferingPatient, Long> {

    List<MentorConnectedSufferingPatient> findByOwnerId(String ownerId);

    List<MentorConnectedSufferingPatient> findByConnectedPatientId(String connectedPatientId);

    Optional<MentorConnectedSufferingPatient> findByOwnerIdAndConnectedPatientId(String ownerId, String connectedPatientId);

    boolean existsByOwnerIdAndConnectedPatientId(String ownerId, String connectedPatientId);
}
