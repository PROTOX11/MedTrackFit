package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_meals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mentorId;

    private String foodName;

    private double foodScore;

    private double dailyScoreAfter;  // the new running score after eating

    private LocalDateTime eatenAt = LocalDateTime.now();

}
