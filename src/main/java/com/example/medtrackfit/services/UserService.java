package com.example.medtrackfit.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.medtrackfit.entities.User;
public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updatUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);

    User updateProfilePicture(String userId, MultipartFile file) throws IOException;
}
