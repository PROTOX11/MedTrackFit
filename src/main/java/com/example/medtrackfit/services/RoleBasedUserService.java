package com.example.medtrackfit.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface RoleBasedUserService {
    UserDetails saveUserByRole(String role, String name, String email, String password, 
                              String phoneNumber, String about, String profilePicture);
    UserDetails getUserByRoleAndEmail(String role, String email);
    boolean isUserExistByRoleAndEmail(String role, String email);
}
