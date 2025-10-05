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
}
