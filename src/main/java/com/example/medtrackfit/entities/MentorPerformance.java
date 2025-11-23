package com.example.medtrackfit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mentor_performance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorPerformance {
    @Id
    @Column(name = "mentor_id")
    private String mentorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mentor_id")
    @JsonBackReference
    private HealthMentor mentor;

    @Column(name = "mentees_helped")
    private int menteesHelped;

    @Column(name = "sessions_conducted")
    private int sessionsConducted;

    @Column(name = "success_rate")
    private double successRate;

    @Column(name = "mentorship_rating")
    private double mentorshipRating;

    @Column(name = "recovery_stories_shared")
    private int recoveryStoriesShared;

    @Column(name = "meditation_score", nullable = true)
    private Integer meditationScore;

    @Column(name = "breathe_score", nullable = true)
    private Integer breatheScore;

    @Column(name = "hydration_score", nullable = true)
    private Integer hydrationScore;

    @Column(name = "nutrition_score", nullable = true)
    private Integer nutritionScore;
}
