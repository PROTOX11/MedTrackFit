package com.example.medtrackfit.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_performance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorPerformance {
    @Id
    @Column(name = "doctor_id")
    private String doctorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @Column(name = "patients_helped")
    private int patientsHelped;

    @Column(name = "consultations_given")
    private int consultationsGiven;

    @Column(name = "satisfaction_rating")
    private double satisfactionRating;

    @Column(name = "years_active")
    private int yearsActive;

    @Column(name = "specialization_score")
    private int specializationScore;

    @Column(name = "total_patients")
    private int totalPatients;

    @Column(name = "active_cases")
    private int activeCases;

    @Column(name = "recovering")
    private int recovering;

    @Column(name = "critical")
    private int critical;

    @Column(name = "rating")
    private double rating;
}
