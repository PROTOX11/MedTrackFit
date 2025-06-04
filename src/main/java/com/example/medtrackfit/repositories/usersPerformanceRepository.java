package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.UsersPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface usersPerformanceRepository extends JpaRepository<UsersPerformance, String> {
    Optional<UsersPerformance> findByUserUserId(String userId);
}