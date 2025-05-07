package com.example.medtrackfit.repo;

import com.example.medtrackfit.entities.ConnectionId;
import com.example.medtrackfit.entities.Connections;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConnectionsRepo extends JpaRepository<Connections, ConnectionId> {
    // Check if a connection exists between two users
    boolean existsByUserUserIdAndConnectedUserUserId(String userId, String connectedId);

    // Find a connection between two users
    Optional<Connections> findByUserUserIdAndConnectedUserUserId(String userId, String connectedId);

    // Find all connections for a user
    List<Connections> findByUserUserId(String userId);

    // Find all users who are connected to a user
    List<Connections> findByConnectedUserUserId(String connectedId);
}