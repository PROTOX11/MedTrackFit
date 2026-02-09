package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_doctor_connections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorDoctorConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentor_id", nullable = false)
    private String mentorId;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt = LocalDateTime.now();

    @Column(name = "status")
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, PENDING
}
