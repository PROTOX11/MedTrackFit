package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmail(String email);
    void deleteByEmail(String email);
}
