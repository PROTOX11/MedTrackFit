package com.example.medtrackfit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String userProfile() {
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
