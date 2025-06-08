package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String fdcId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double quantity;

    @Column(nullable = false)
    private double calories;

    @Column(nullable = false)
    private LocalDateTime entryDateTime;
}
