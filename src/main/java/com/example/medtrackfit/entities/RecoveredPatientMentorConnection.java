package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recovered_patient_mentor_connections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecoveredPatientMentorConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recovered_patient_id", nullable = false)
    private String recoveredPatientId;

    @Column(name = "mentor_id", nullable = false)
    private String mentorId;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt = LocalDateTime.now();

    @Column(name = "status")
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, PENDING
}
