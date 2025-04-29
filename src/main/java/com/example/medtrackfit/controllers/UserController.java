package com.example.medtrackfit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.services.UserService;
import com.medtrackfit.helper.Helper;

import jakarta.persistence.criteria.Path;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @ModelAttribute
    public void addLoggedinUserInformation(Model model, Authentication authentication) {
        System.out.println("Adding logged in user information to model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
        User user=userService.getUserByEmail(username);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("LoggedInUser", user);
    }

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        System.out.println("User dashboard");
        return "user/dashboard";
    }



    @RequestMapping(value="/profile")
    public String userProfile(Model model, Authentication authentication)
    {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
        User user=userService.getUserByEmail(username);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);
        return "user/profile";
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Authentication authentication,
            Model model) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        try {
            if (!file.isEmpty()) {
                // Validate file type
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed");
                    model.addAttribute("loggedInUser", user);
                    return "user/profile";
                }

                userService.updateProfilePicture((user.getUserId()), file);

                logger.info("profile picture updated for user: {}", username);

            } else {
                model.addAttribute("error", "No file uploaded");
                model.addAttribute("loggedInUser", user);
                return "user/profile";
            } 
        } catch (Exception e) {
            logger.error("Failed to update profile picture for user: {}", username, e);
            model.addAttribute("error", "Failed to update profile picture: ");
            model.addAttribute("loggedInUser", user);
            return "user/profile";
        } 
        
        return "redirect:/user/profile";
    }
    @RequestMapping("/settings")
    public String settings() {
        return "user/settings";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "user/logout";
    }
    @RequestMapping("/chat")
    public String chat() {
        return "user/chat";
    }
    @RequestMapping("/recpatient")
    public String recpatient() {
        return "user/recpatient";
    }
}
