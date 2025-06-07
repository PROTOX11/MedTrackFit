package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT SUM(f.totalCalories) FROM FoodEntry f WHERE f.userId = :userId AND f.createdAt >= :startDate AND f.createdAt <= :endDate")
    Double getTotalCaloriesForDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM FoodEntry f WHERE f.userId = :userId AND f.createdAt >= :startDate AND f.createdAt <= :endDate ORDER BY f.createdAt DESC")
    List<FoodEntry> getFoodEntriesForDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
