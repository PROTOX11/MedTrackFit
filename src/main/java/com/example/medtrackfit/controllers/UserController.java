package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.MentorPerformance;
import com.example.medtrackfit.entities.UsersPerformance;
import com.example.medtrackfit.services.HealthMentorService;
import com.example.medtrackfit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HealthMentorService healthMentorService;

    private boolean isMentorId(String id) {
        return id != null && id.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }

    @GetMapping("/performance/{userId}")
    public ResponseEntity<?> getUserPerformance(@PathVariable String userId) {
        try {
            if (isMentorId(userId)) {
                Object performanceData = healthMentorService.getMentorPerformance(userId);
                return ResponseEntity.ok(performanceData);
            } else {
                UsersPerformance performance = userService.getUserPerformance(userId);
                return ResponseEntity.ok(performance);
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/meditation")
    public ResponseEntity<Map<String, String>> saveMeditationTime(@PathVariable String userId,
                                                                 @RequestBody Map<String, Integer> payload) {
        try {
            int meditationTime = payload.get("meditationTime");
            if (isMentorId(userId)) {
                healthMentorService.updateMeditationScore(userId, meditationTime);
            } else {
                userService.updateMeditationTime(userId, meditationTime);
            }
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{userId}/breathescore")
    public ResponseEntity<Map<String, String>> saveBreatheScore(@PathVariable String userId,
                                                               @RequestBody Map<String, Integer> payload) {
        try {
            int breatheScore = payload.get("breatheScore");
            if (isMentorId(userId)) {
                healthMentorService.updateBreatheScore(userId, breatheScore);
            } else {
                // TODO: Implement breathe score for regular users
                // userService.updateBreatheScore(userId, breatheScore);
                throw new UnsupportedOperationException("Breathe score not implemented for regular users");
            }
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{userId}/hydration")
    public ResponseEntity<Map<String, String>> saveHydration(@PathVariable String userId,
                                                            @RequestBody Map<String, Integer> payload) {
        try {
            int hydrationAmount = payload.get("hydrationAmount");
            if (isMentorId(userId)) {
                healthMentorService.updateHydrationScore(userId, hydrationAmount);
            } else {
                // TODO: Implement hydration score for regular users
                // userService.updateHydrationScore(userId, hydrationAmount);
                throw new UnsupportedOperationException("Hydration score not implemented for regular users");
            }
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/mentor/{mentorId}/food/save")
    public ResponseEntity<Map<String, Object>> saveMentorFoodEntries(@PathVariable String mentorId,
                                                                    @RequestBody List<Map<String, Object>> foodEntries) {
        try {
            // Calculate nutrition score from food entries
            double totalNutritionScore = calculateNutritionScore(foodEntries);
            int score = Math.min(10, (int) Math.round(totalNutritionScore));

            healthMentorService.updateNutritionScore(mentorId, score);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("nutritionScore", score);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/food/save")
    public ResponseEntity<Map<String, Object>> saveFoodEntries(@RequestParam String userId,
                                                              @RequestBody List<Map<String, Object>> foodEntries) {
        try {
            // Calculate nutrition score from food entries
            double totalNutritionScore = calculateNutritionScore(foodEntries);
            int score = Math.min(10, (int) Math.round(totalNutritionScore));

            userService.updateNutritionScore(userId, score);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("nutritionScore", score);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Food search is implemented in MentorController
    // @GetMapping("/food/search")
    // public ResponseEntity<List<Map<String, Object>>> searchFood(@RequestParam String query) {
    //     try {
    //         List<Map<String, Object>> results = userService.searchFood(query);
    //         return ResponseEntity.ok(results);
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }

    private double calculateNutritionScore(List<Map<String, Object>> foodEntries) {
        double totalScore = 0;
        for (Map<String, Object> entry : foodEntries) {
            // Simple calculation based on nutritional values
            double calories = ((Number) entry.getOrDefault("calories", 0)).doubleValue();
            double protein = ((Number) entry.getOrDefault("protein", 0)).doubleValue();
            double fiber = ((Number) entry.getOrDefault("fiber", 0)).doubleValue();
            double vitamins = ((Number) entry.getOrDefault("vitamins", 0)).doubleValue();

            // Normalize to a score out of 10
            double score = (calories * 0.3 + protein * 0.3 + fiber * 0.2 + vitamins * 0.2) / 100;
            totalScore += Math.min(10, score);
        }
        return Math.min(10, totalScore);
    }
}
