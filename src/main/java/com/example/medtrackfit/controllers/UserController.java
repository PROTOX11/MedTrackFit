package com.example.medtrackfit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.services.UserService;
import com.medtrackfit.helper.Helper;

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
