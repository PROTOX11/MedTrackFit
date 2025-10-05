package com.example.medtrackfit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recovered_patient_performance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecoveredPatientPerformance {
    @Id
    @Column(name = "patient_id")
    private String patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private RecoveredPatient recoveredPatient;

    @Column(name = "recovery_score")
    private int recoveryScore;

    @Column(name = "mentoring_sessions")
    private int mentoringSessions;

    @Column(name = "lives_impacted")
    private int livesImpacted;

    @Column(name = "mentor_rating")
    private double mentorRating;

    @Column(name = "story_views")
    private int storyViews;
}
