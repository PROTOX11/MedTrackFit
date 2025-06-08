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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.medtrackfit.helper.AppConstants;
import com.medtrackfit.helper.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public class MeditationTimeDTO {
        @JsonProperty("meditationTime")
        private int meditationTime;

        public int getMeditationTime() {
            return meditationTime;
        }

        public void setMeditationTime(int meditationTime) {
            this.meditationTime = meditationTime;
        }
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info("Encoded password: {}", user.getPassword());

        if (user.getUsersPerformance() == null) {
            UsersPerformance performance = UsersPerformance.builder()
                    .user(user)
                    .healthScore(0)
                    .goalProgress(0)
                    .nutritionScore(0)
                    .breatheScore(0)
                    .meditationScore(0)
                    .hydrationScore(0)
                    .build();
            user.setUsersPerformance(performance);
        }
        User savedUser = userRepo.save(user);
        logger.info("User saved with userId: {}, has performance: {}",
                savedUser.getUserId(), savedUser.getUsersPerformance() != null);
        return savedUser;
    }

    @Override
    @Transactional
    public Connections addConnection(String userId, String connectedId) {
        if (userId.equals(connectedId)) {
            throw new IllegalArgumentException("User cannot connect to themselves");
        }
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        User connectedUser = userRepo.findById(connectedId)
                .orElseThrow(() -> new ResourceNotFoundException("Connected user not found: " + connectedId));

        if (connectionsRepo.existsByUserUserIdAndConnectedUserUserId(userId, connectedId)) {
            throw new IllegalStateException("Connection already exists between " + userId + " and " + connectedId);
        }
        logger.info("User ID: {}", user.getUserId());
        logger.info("Connected User ID: {}", connectedUser.getUserId());

        Connections connection = Connections.builder()
                .user(user)
                .connectedUser(connectedUser)
                .build();
        ConnectionId connectionId = connection.getId();
        if (connectionId.getUserId() == null || connectionId.getConnectedId() == null) {
            connectionId.setUserId(user.getUserId());
            connectionId.setConnectedId(connectedUser.getUserId());
        }
        logger.info("Connection ID after building - userId: {}, connectedId: {}",
                connection.getId().getUserId(), connection.getId().getConnectedId());
        Connections savedConnection = connectionsRepo.save(connection);
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

        if (user.getUsersPerformance() != null) {
            UsersPerformance existingPerformance = user2.getUsersPerformance();
            if (existingPerformance != null) {
                UsersPerformance updatedPerformance = user.getUsersPerformance();
                existingPerformance.setHealthScore(updatedPerformance.getHealthScore());
                existingPerformance.setGoalProgress(updatedPerformance.getGoalProgress());
                existingPerformance.setNutritionScore(updatedPerformance.getNutritionScore());
                existingPerformance.setBreatheScore(updatedPerformance.getBreatheScore());
                existingPerformance.setMeditationScore(updatedPerformance.getMeditationScore());
                existingPerformance.setHydrationScore(updatedPerformance.getHydrationScore());
            } else {
                UsersPerformance newPerformance = UsersPerformance.builder()
                        .user(user2)
                        .healthScore(user.getUsersPerformance().getHealthScore())
                        .goalProgress(user.getUsersPerformance().getGoalProgress())
                        .nutritionScore(user.getUsersPerformance().getNutritionScore())
                        .breatheScore(user.getUsersPerformance().getBreatheScore())
                        .meditationScore(user.getUsersPerformance().getMeditationScore())
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
        logger.info("Fetching user by email: {}", email);
        User user = userRepo.findByEmail(email).orElse(null);
        logger.info("User fetched: {}", user != null ? user.getEmail() : "null");
        return user;
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
    @Transactional
    public UsersPerformance getUserPerformance(String userId) {
        logger.info("Fetching performance for userId: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        UsersPerformance performance = user.getUsersPerformance();
        if (performance == null) {
            logger.warn("User performance not found for userId: {}. Creating new performance.", userId);
            performance = UsersPerformance.builder()
                    .user(user)
                    .healthScore(0)
                    .goalProgress(0)
                    .nutritionScore(0)
                    .breatheScore(0)
                    .meditationScore(0)
                    .hydrationScore(0)
                    .build();
            user.setUsersPerformance(performance);
            userRepo.save(user);
            logger.info("Created and saved new performance for userId: {}", userId);
        }
        logger.info("Performance fetched: meditationScore: {}", performance.getMeditationScore());
        return performance;
    }

    @Override
    @Transactional
    public void updateMeditationTime(String userId, int meditationTime) {
        logger.info("Updating meditation time for userId: {}, time: {}", userId, meditationTime);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        
        UsersPerformance performance = user.getUsersPerformance();
        if (performance == null) {
            logger.info("Creating new UsersPerformance for userId: {}", userId);
            performance = UsersPerformance.builder()
                    .user(user)
                    .healthScore(0)
                    .goalProgress(0)
                    .nutritionScore(0)
                    .breatheScore(0)
                    .meditationScore(0)
                    .hydrationScore(0)
                    .build();
            user.setUsersPerformance(performance);
        }

        int currentMeditationScore = performance.getMeditationScore();
        performance.setMeditationScore(currentMeditationScore + meditationTime); // Add to existing value
        logger.info("Meditation score updated to: {} for userId: {}", performance.getMeditationScore(), userId);
        
        try {
            userRepo.flush();
            User savedUser = userRepo.save(user);
            logger.info("User and performance saved for userId: {}, meditationScore: {}", 
                        userId, savedUser.getUsersPerformance().getMeditationScore());
        } catch (Exception e) {
            logger.error("Error saving meditation time to database for userId: {}", userId, e);
            throw new RuntimeException("Failed to save meditation time to database", e);
        }
    }

    @Override
    @Transactional
    public void updateNutritionScore(String userId, int nutritionScore) {
        logger.info("Updating nutrition score for userId: {}, score to add: {}", userId, nutritionScore);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        UsersPerformance performance = user.getUsersPerformance();
        if (performance == null) {
            logger.info("Creating new UsersPerformance for userId: {}", userId);
            performance = UsersPerformance.builder()
                    .user(user)
                    .healthScore(0)
                    .goalProgress(0)
                    .nutritionScore(nutritionScore)  // Set initial score
                    .breatheScore(0)
                    .meditationScore(0)
                    .hydrationScore(0)
                    .build();
            user.setUsersPerformance(performance);
        } else {
            // Add new score to existing score
            int currentScore = performance.getNutritionScore();
            int newScore = currentScore + nutritionScore;
            performance.setNutritionScore(newScore);
            logger.info("Updated nutrition score from {} to {} for userId: {}", currentScore, newScore, userId);
        }

        try {
            userRepo.save(user);  // Save user which will cascade to performance
            logger.info("Successfully saved nutrition score for userId: {}", userId);
        } catch (Exception e) {
            logger.error("Error saving nutrition score to database for userId: {}", userId, e);
            throw new RuntimeException("Failed to save nutrition score to database", e);
        }
    }

}