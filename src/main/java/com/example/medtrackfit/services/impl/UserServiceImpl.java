package com.example.medtrackfit.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.medtrackfit.entities.ConnectionId;
import com.example.medtrackfit.entities.Connections;
import com.example.medtrackfit.entities.Providers;
import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.entities.UsersPerformance;
import com.example.medtrackfit.repo.ConnectionsRepo;
import com.example.medtrackfit.repo.UserRepo;
import com.example.medtrackfit.services.CloudinaryService;
import com.example.medtrackfit.services.UserService;
import com.medtrackfit.helper.AppConstants;
import com.medtrackfit.helper.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ConnectionsRepo connectionsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public User saveUser(User user) {
        
        // Check if a user with this email already exists
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }

        // Password encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info("Encoded password: {}", user.getPassword());

        // Create associated UsersPerformance entity if not present
        if (user.getUsersPerformance() == null) {
            UsersPerformance performance = UsersPerformance.builder()
                    .user(user)
                    .healthScore(0)
                    .goalProgress(0)
                    .stepsScore(0)
                    .sleepScore(0)
                    .exerciseScore(0)
                    .hydrationScore(0)
                    .build();
            user.setUsersPerformance(performance);
        }

        // Save the user (Hibernate will generate the userId)
        return userRepo.save(user);
    }

    @Override
    @Transactional
    public Connections addConnection(String userId, String connectedId) {
        if (userId.equals(connectedId)) {
            throw new IllegalArgumentException("User cannot connect to themselves");
        }

        // Fetch users within the transaction
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        User connectedUser = userRepo.findById(connectedId)
                .orElseThrow(() -> new ResourceNotFoundException("Connected user not found: " + connectedId));

        // Check if the connection already exists
        if (connectionsRepo.existsByUserUserIdAndConnectedUserUserId(userId, connectedId)) {
            throw new IllegalStateException("Connection already exists between " + userId + " and " + connectedId);
        }

        // Log the user IDs to verify
        logger.info("User ID: {}", user.getUserId());
        logger.info("Connected User ID: {}", connectedUser.getUserId());

        // Build the Connections entity
        Connections connection = Connections.builder()
                .user(user)
                .connectedUser(connectedUser)
                .build();

        // Ensure the ConnectionId is populated by @MapsId
        ConnectionId connectionId = connection.getId();
        if (connectionId.getUserId() == null || connectionId.getConnectedId() == null) {
            connectionId.setUserId(user.getUserId());
            connectionId.setConnectedId(connectedUser.getUserId());
        }

        // Log the ConnectionId after building
        logger.info("Connection ID after building - userId: {}, connectedId: {}",
                connection.getId().getUserId(), connection.getId().getConnectedId());

        // Save the connection
        Connections savedConnection = connectionsRepo.save(connection);

        // Verify the saved values
        logger.info("Saved Connection - userId: {}, connectedId: {}",
                savedConnection.getId().getUserId(), savedConnection.getId().getConnectedId());

        return savedConnection;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> updatUser(User user) {
        User user2 = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update user fields
        user2.setName(user.getName());
        user2.setRole(user.getRole());
        user2.setEmail(user.getEmail());
        user2.setPassword(
                user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : user2.getPassword());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setAbout(user.getAbout());
        user2.setProfilePicture(user.getProfilePicture());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider() != null ? user.getProvider() : Providers.SELF);
        user2.setProviderUserId(user.getProviderUserId());

        // Update UsersPerformance if provided
        if (user.getUsersPerformance() != null) {
            UsersPerformance existingPerformance = user2.getUsersPerformance();
            if (existingPerformance != null) {
                UsersPerformance updatedPerformance = user.getUsersPerformance();
                existingPerformance.setHealthScore(updatedPerformance.getHealthScore());
                existingPerformance.setGoalProgress(updatedPerformance.getGoalProgress());
                existingPerformance.setStepsScore(updatedPerformance.getStepsScore());
                existingPerformance.setSleepScore(updatedPerformance.getSleepScore());
                existingPerformance.setExerciseScore(updatedPerformance.getExerciseScore());
                existingPerformance.setHydrationScore(updatedPerformance.getHydrationScore());
            } else {
                // If no existing performance, create a new one
                UsersPerformance newPerformance = UsersPerformance.builder()
                        .user(user2)
                        .healthScore(user.getUsersPerformance().getHealthScore())
                        .goalProgress(user.getUsersPerformance().getGoalProgress())
                        .stepsScore(user.getUsersPerformance().getStepsScore())
                        .sleepScore(user.getUsersPerformance().getSleepScore())
                        .exerciseScore(user.getUsersPerformance().getExerciseScore())
                        .hydrationScore(user.getUsersPerformance().getHydrationScore())
                        .build();
                user2.setUsersPerformance(newPerformance);
            }
        }

        User saved = userRepo.save(user2);
        return Optional.ofNullable(saved);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserExist(String userId) {
        return userRepo.findById(userId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public User updateProfilePicture(String userId, MultipartFile file) throws IOException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = cloudinaryService.uploadImage(file);
        user.setProfilePicture(imageUrl);
        return userRepo.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UsersPerformance getUserPerformance(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UsersPerformance performance = user.getUsersPerformance();
        if (performance == null) {
            throw new ResourceNotFoundException("User performance not found for user: " + userId);
        }
        return performance;
    }
}