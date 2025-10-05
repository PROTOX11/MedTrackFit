package com.example.medtrackfit.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.repo.UserRepo;
import com.example.medtrackfit.services.UniversalUserService;


@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    @Lazy
    private UniversalUserService universalUserService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find user in the regular User table
        try {
            User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), 
                user.getAuthorities()
            );
        } catch (UsernameNotFoundException e) {
            // If not found in User table, try UniversalUserService for role-based collections
            UserDetails userDetails = universalUserService.getUserByEmail(username);
            if (userDetails != null) {
                return userDetails;
            }
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }

}
