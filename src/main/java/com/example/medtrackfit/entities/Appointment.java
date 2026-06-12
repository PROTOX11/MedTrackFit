package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String doctorId;

    private String doctorName;

    private String patientName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String time; // e.g. "11:30 AM - 12:00 PM"

    @Column(nullable = false)
    private String date; // e.g. "2026-06-11"
}
