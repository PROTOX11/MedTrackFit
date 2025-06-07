package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_entries")
public class FoodEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "fdc_id")
    private String fdcId;

    @Column(name = "calories_per_100g", nullable = false)
    private Double caloriesPer100g;

    @Column(name = "quantity_grams", nullable = false)
    private Double quantityGrams;

    @Column(name = "total_calories", nullable = false)
    private Double totalCalories;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public FoodEntry() {}

    public FoodEntry(Long userId, String foodName, String fdcId, Double caloriesPer100g, Double quantityGrams) {
        this.userId = userId;
        this.foodName = foodName;
        this.fdcId = fdcId;
        this.caloriesPer100g = caloriesPer100g;
        this.quantityGrams = quantityGrams;
        this.totalCalories = (caloriesPer100g * quantityGrams) / 100;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFdcId() {
        return fdcId;
    }

    public void setFdcId(String fdcId) {
        this.fdcId = fdcId;
    }

    public Double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public void setCaloriesPer100g(Double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public Double getQuantityGrams() {
        return quantityGrams;
    }

    public void setQuantityGrams(Double quantityGrams) {
        this.quantityGrams = quantityGrams;
        if (this.caloriesPer100g != null) {
            this.totalCalories = (this.caloriesPer100g * quantityGrams) / 100;
        }
    }

    public Double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
