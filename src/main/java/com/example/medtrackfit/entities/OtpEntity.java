package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
