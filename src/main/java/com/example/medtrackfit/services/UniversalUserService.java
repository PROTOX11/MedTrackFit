package com.example.medtrackfit.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UniversalUserService {
    UserDetails getUserByEmail(String email);
    boolean isUserExistByEmail(String email);
    String getUserRole(String email);
    String getUserId(String email);
    String getUserName(String email);
}
