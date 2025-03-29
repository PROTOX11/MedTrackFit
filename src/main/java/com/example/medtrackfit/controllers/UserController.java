package com.example.medtrackfit.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.medtrackfit.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        System.out.println("User dashboard");
        return "user/dashboard";
    }

    @RequestMapping(value="/profile")
    public String userProfile(Authentication authentication) 
    {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        
        logger.info("User logged in: {}", username);
        
        System.out.println("User profile");
        return "user/profile";
    }

    @RequestMapping("/settings")
    public String settings() {
        return "user/settings";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "user/logout";
    }
}
