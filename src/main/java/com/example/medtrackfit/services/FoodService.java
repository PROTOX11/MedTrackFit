package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.FoodEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.medtrackfit.repositories.FoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FoodService {
    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    private final String USDA_BASE_URL = "https://api.nal.usda.gov/fdc/v1";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FoodEntryRepository foodEntryRepository;
    private final UserService userService;

    @Value("${usda.api.key}")
    private String apiKey;

    @Autowired
    public FoodService(
            FoodEntryRepository foodEntryRepository,
            UserService userService,
            RestTemplate restTemplate) {
        this.foodEntryRepository = foodEntryRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public List<Map<String, Object>> searchFoods(String query) {
        try {
            String url = String.format("%s/foods/search?api_key=%s&query=%s&pageSize=6",
                    USDA_BASE_URL, apiKey, query);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            List<Map<String, Object>> results = new ArrayList<>();

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode foods = root.get("foods");

            if (foods != null && foods.isArray()) {
                int count = 0;
                for (JsonNode food : foods) {
                    if (count >= 6) break;

                    Map<String, Object> foodData = new HashMap<>();
                    foodData.put("fdcId", food.get("fdcId").asText());
                    foodData.put("description", food.get("description").asText());

                    // Extract calories
                    Double calories = extractCalories(food.get("foodNutrients"));
                    foodData.put("calories", calories != null ? calories : 0.0);
                    foodData.put("quantity", 100); // Default quantity in grams

                    results.add(foodData);
                    count++;
                }
            }
            return results;
        } catch (Exception e) {
            logger.error("Error searching foods: ", e);
            throw new RuntimeException("Failed to search foods", e);
        }
    }

    private Double extractCalories(JsonNode foodNutrients) {
        if (foodNutrients != null && foodNutrients.isArray()) {
            for (JsonNode nutrient : foodNutrients) {
                if (nutrient.get("nutrientName") != null &&
                        nutrient.get("nutrientName").asText().equals("Energy") &&
                        nutrient.get("unitName") != null &&
                        nutrient.get("unitName").asText().equals("KCAL")) {
                    return nutrient.get("value").asDouble();
                }
            }
        }
        return null;
    }

    @Transactional
    public void saveFoodEntry(String userId, List<Map<String, Object>> foods) {
        try {
            double totalCalories = 0.0;
            LocalDateTime now = LocalDateTime.now();

            for (Map<String, Object> food : foods) {
                FoodEntry entry = new FoodEntry();
                entry.setUserId(userId);
                entry.setFdcId(food.get("fdcId").toString());
                entry.setDescription(food.get("description").toString());
                double quantity = Double.parseDouble(food.get("quantity").toString());
                double caloriesPer100g = Double.parseDouble(food.get("calories").toString());
                entry.setQuantity(quantity);
                entry.setCalories(caloriesPer100g);
                entry.setEntryDateTime(now);
                foodEntryRepository.save(entry);
                // Calculate actual calories based on quantity
                double foodCalories = (quantity / 100.0) * caloriesPer100g;
                totalCalories += foodCalories;
            }

            // Update user's nutrition score
            userService.updateNutritionScore(userId, (int) totalCalories);
            logger.info("Saved food entries for user: {}, total calories: {}", userId, totalCalories);
        } catch (Exception e) {
            logger.error("Error saving food entries for user: {}", userId, e);
            throw new RuntimeException("Failed to save food entries", e);
        }
    }
}
