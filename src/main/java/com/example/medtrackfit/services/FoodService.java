package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.FoodEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.medtrackfit.repositories.FoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FoodService {

    @Value("${usda.api.key}")
    private String apiKey;

    private final String USDA_BASE_URL = "https://api.nal.usda.gov/fdc/v1";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FoodEntryRepository foodEntryRepository;

    @Autowired
    public FoodService(FoodEntryRepository foodEntryRepository) {
        this.foodEntryRepository = foodEntryRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Map<String, Object>> searchFoods(String query) {
        String url = String.format("%s/foods/search?api_key=%s&query=%s&pageSize=10", USDA_BASE_URL, apiKey, query);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode foods = root.get("foods");
            
            if (foods != null && foods.isArray()) {
                for (JsonNode food : foods) {
                    String fdcId = food.get("fdcId").asText();
                    String description = food.get("description").asText();
                    
                    // Get calories if available
                    Double calories = null;
                    JsonNode foodNutrients = food.get("foodNutrients");
                    if (foodNutrients != null && foodNutrients.isArray()) {
                        for (JsonNode nutrient : foodNutrients) {
                            if (nutrient.get("nutrientName") != null && 
                                nutrient.get("nutrientName").asText().equals("Energy") &&
                                nutrient.get("unitName") != null &&
                                nutrient.get("unitName").asText().equals("KCAL")) {
                                calories = nutrient.get("value").asDouble();
                                break;
                            }
                        }
                    }

                    Map<String, Object> foodData = Map.of(
                        "fdcId", fdcId,
                        "description", description,
                        "calories", calories != null ? calories : 0.0
                    );
                    results.add(foodData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public FoodEntry addFoodEntry(Long userId, String foodName, String fdcId, Double caloriesPer100g, Double quantityGrams) {
        FoodEntry entry = new FoodEntry(userId, foodName, fdcId, caloriesPer100g, quantityGrams);
        return foodEntryRepository.save(entry);
    }

    public Double getTodaysTotalCalories(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return foodEntryRepository.getTotalCaloriesForDateRange(userId, startOfDay, endOfDay);
    }

    public List<FoodEntry> getTodaysFoodEntries(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return foodEntryRepository.getFoodEntriesForDateRange(userId, startOfDay, endOfDay);
    }
}
