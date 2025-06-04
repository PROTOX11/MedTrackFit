package com.example.medtrackfit.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.medtrackfit.entities.Connections;
import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.entities.UsersPerformance;


public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updatUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    UsersPerformance getUserPerformance(String userId);
    Connections addConnection(String userId, String connectedId);
    User updateProfilePicture(String userId, MultipartFile file) throws IOException;
    void updateMeditationTime(String userId, int meditationTime);
}
