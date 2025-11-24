package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_connected_suffering_patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorConnectedSufferingPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId; // mentor_id

    @Column(name = "connected_patient_id", nullable = false)
    private String connectedPatientId; // suffering_patient_id

    @Column(name = "connected_at")
    private LocalDateTime connectedAt = LocalDateTime.now();

    @Column(name = "status")
    private String status = "ACTIVE"; // ACTIVE, INACTIVE
}
