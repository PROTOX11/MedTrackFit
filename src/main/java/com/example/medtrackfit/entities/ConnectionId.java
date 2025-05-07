package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConnectionId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "connected_id")
    private String connectedId;
}