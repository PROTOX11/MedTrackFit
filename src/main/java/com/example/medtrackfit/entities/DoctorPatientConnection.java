package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor_connected_patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorPatientConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_patient_ids", joinColumns = @JoinColumn(name = "connection_id"))
    @Column(name = "patient_id")
    @Builder.Default
    private List<String> patientIds = new ArrayList<>();

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
