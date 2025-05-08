package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_performance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPerformance {
    @Id
    @Column(name = "user_id")
    private String userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "health_score")
    private int healthScore;

    @Column(name = "goal_progress")
    private int goalProgress;

    @Column(name = "steps_score")
    private int stepsScore;
    
    @Column(name = "sleep_score")
    private int sleepScore;

    @Column(name = "exercise_score")
    private int exerciseScore;

    @Column(name = "hydration_score")
    private int hydrationScore;

}