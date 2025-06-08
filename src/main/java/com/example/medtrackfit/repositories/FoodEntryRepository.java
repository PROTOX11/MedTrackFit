package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserIdOrderByEntryDateTimeDesc(String userId);

    @Query("SELECT SUM(f.calories * f.quantity / 100.0) FROM FoodEntry f WHERE f.userId = :userId AND f.entryDateTime BETWEEN :startDate AND :endDate")
    Double getTotalCaloriesForDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT f FROM FoodEntry f WHERE f.userId = :userId AND f.entryDateTime BETWEEN :startDate AND :endDate")
    List<FoodEntry> getFoodEntriesForDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);
}
