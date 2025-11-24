package com.example.medtrackfit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_performance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientPerformance {
    @Id
    @Column(name = "patient_id")
    private String patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private SufferingPatient sufferingPatient;

    @Column(name = "health_score")
    private int healthScore;

    @Column(name = "treatment_progress")
    private int treatmentProgress;

    @Column(name = "sessions_attended")
    private int sessionsAttended;

    @Column(name = "medication_adherence")
    private double medicationAdherence;

    @Column(name = "overall_improvement")
    private double overallImprovement;

    // Added fields to track per-user health metrics (meditation, breathwork, hydration, nutrition)
    @Column(name = "meditation_score")
    private Integer meditationScore = 0;

    @Column(name = "breathe_score")
    private Integer breatheScore = 0;

    @Column(name = "hydration_score")
    private Integer hydrationScore = 0;

    @Column(name = "nutrition_score")
    private Integer nutritionScore = 0;
}
