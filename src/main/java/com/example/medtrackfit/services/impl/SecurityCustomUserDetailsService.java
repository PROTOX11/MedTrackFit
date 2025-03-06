package com.example.medtrackfit.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.repo.UserRepo;


@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with emails"));
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), 
            user.getPassword(), 
            user.getAuthorities()
            );
    }

}
