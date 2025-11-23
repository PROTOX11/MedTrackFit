package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {

    List<MealEntry> findByMentorIdAndEatenAtBetween(String mentorId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(m.foodScore) FROM MealEntry m WHERE m.mentorId = :mentorId AND DATE(m.eatenAt) = DATE(CURRENT_DATE)")
    OptionalDouble findAverageScoreByMentorIdAndToday(@Param("mentorId") String mentorId);

    List<MealEntry> findByMentorIdOrderByEatenAtDesc(String mentorId);
}
