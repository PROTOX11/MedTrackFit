package com.example.medtrackfit.controllers;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;

@ControllerAdvice
public class RootController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UniversalUserService universalUserService;
    
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        System.out.println("Adding logged in user information to model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
        
        // Check if the user is logged in using universal service
        UserDetails userDetails = universalUserService.getUserByEmail(username);
        if (userDetails != null) {
            String userRole = universalUserService.getUserRole(username);
            String userName = universalUserService.getUserName(username);
            String userId = universalUserService.getUserId(username);
            
            System.out.println("User found: " + userName);
            System.out.println("User role: " + userRole);
            System.out.println("User email: " + username);
            
            // Add user information to model
            model.addAttribute("loggedInUser", userDetails);
            model.addAttribute("userRole", userRole);
            model.addAttribute("userName", userName);
            model.addAttribute("userId", userId);
        } else {
            logger.warn("User not found for email: {}", username);
        }
    }

}
