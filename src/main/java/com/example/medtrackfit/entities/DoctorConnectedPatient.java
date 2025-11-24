package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_connected_patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorConnectedPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "connected_patient_id", nullable = false)
    private String connectedPatientId;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt = LocalDateTime.now();

    @Column(name = "status")
    private String status = "ACTIVE";
}
