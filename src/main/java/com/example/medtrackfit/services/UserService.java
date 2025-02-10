package com.example.medtrackfit.services;

import java.util.List;
import java.util.Optional;

import com.example.medtrackfit.entities.User;
public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updatUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
}
