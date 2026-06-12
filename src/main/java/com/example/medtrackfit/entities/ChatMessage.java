package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderEmail;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String senderName;
}
