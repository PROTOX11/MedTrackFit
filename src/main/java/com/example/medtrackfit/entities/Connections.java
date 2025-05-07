package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "connections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Connections {

    @EmbeddedId
    @Builder.Default
    private ConnectionId id = new ConnectionId(); // Initialize with default constructor

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("connectedId")
    @JoinColumn(name = "connected_id", nullable = false)
    private User connectedUser;
}