package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.MealEntry;
import com.example.medtrackfit.repositories.MealEntryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Service
public class NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionService.class);

    @Autowired
    private MealEntryRepository repo;

    // Store current scores per mentor
    private final Map<String, Double> mentorScores = new HashMap<>();

    @PostConstruct
    public void initTodayScores() {
        logger.info("Initializing today's nutrition scores for all mentors");

        // Get all unique mentor IDs and initialize their scores
        List<String> mentorIds = repo.findAll().stream()
            .map(MealEntry::getMentorId)
            .distinct()
            .toList();

        for (String mentorId : mentorIds) {
            initializeMentorScore(mentorId);
        }

        logger.info("Initialized scores for {} mentors", mentorIds.size());
    }

    private void initializeMentorScore(String mentorId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Find all meal entries for today for this mentor
        List<MealEntry> todayMeals = repo.findByMentorIdAndEatenAtBetween(mentorId, startOfDay, endOfDay);

        if (!todayMeals.isEmpty()) {
            // Get the most recent meal entry for today
            MealEntry lastMeal = todayMeals.stream()
                .max((a, b) -> a.getEatenAt().compareTo(b.getEatenAt()))
                .orElse(null);

            if (lastMeal != null) {
                mentorScores.put(mentorId, lastMeal.getDailyScoreAfter());
                logger.info("Initialized score for mentor {}: {}", mentorId, lastMeal.getDailyScoreAfter());
            } else {
                mentorScores.put(mentorId, 10.0); // Default starting score
                logger.info("No valid meals today for mentor {}, starting with default score: 10.0", mentorId);
            }
        } else {
            mentorScores.put(mentorId, 10.0); // Default starting score
            logger.info("New mentor {}, starting with default score: 10.0", mentorId);
        }
    }

    @Transactional
    public synchronized double eatFood(String mentorId, String foodName, double foodScore) {
        // Initialize score if not exists
        if (!mentorScores.containsKey(mentorId)) {
            initializeMentorScore(mentorId);
        }

        double currentScore = mentorScores.get(mentorId);

        // Calculate new score as average of current + food score
        double newScore = (currentScore + foodScore) / 2.0;

        // Round to 2 decimal places
        newScore = Math.round(newScore * 100.0) / 100.0;

        // Create meal entry
        MealEntry entry = MealEntry.builder()
            .mentorId(mentorId)
            .foodName(foodName)
            .foodScore(foodScore)
            .dailyScoreAfter(newScore)
            .build();

        repo.save(entry);

        // Update current score
        mentorScores.put(mentorId, newScore);

        logger.info("Mentor {} ate {} (score: {}). Daily score: {} -> {}",
                   mentorId, foodName, foodScore, currentScore, newScore);

        return newScore;
    }

    public double getCurrentScore(String mentorId) {
        if (!mentorScores.containsKey(mentorId)) {
            initializeMentorScore(mentorId);
        }

        double score = mentorScores.get(mentorId);
        return Math.round(score * 100.0) / 100.0;
    }

    public List<MealEntry> getTodayMeals(String mentorId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return repo.findByMentorIdAndEatenAtBetween(mentorId, startOfDay, endOfDay);
    }
}
