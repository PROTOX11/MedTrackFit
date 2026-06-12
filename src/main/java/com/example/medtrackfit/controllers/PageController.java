package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.RoleBasedUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serves the React SPA shell for all public-facing page routes.
 * The React router (App.jsx) handles all client-side navigation.
 */
@Controller
public class PageController {

    @Autowired
    private RoleBasedUserService roleBasedUserService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        System.out.println("About page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @RequestMapping("/services")
    public String servicesPage(Model model) {
        System.out.println("Services page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @RequestMapping("/features")
    public String featuresPage(Model model) {
        System.out.println("Features page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @RequestMapping("/privacy")
    public String privacyPage(Model model) {
        System.out.println("Privacy page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @RequestMapping("/contact")
    public String contactPage(Model model) {
        System.out.println("Contact page → react_shell");
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @GetMapping("/auth")
    public String authPage(Model model) {
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    @GetMapping("/terms")
    public String termsPage(Model model) {
        model.addAttribute("name", "");
        model.addAttribute("email", "");
        model.addAttribute("role", "");
        model.addAttribute("profilePicture", "");
        model.addAttribute("id", "");
        return "react_shell";
    }

    // Keep logged_home as a redirect to avoid 404 on old bookmarks
    @RequestMapping("/logged_home")
    public String loggedHome() {
        return "redirect:/home";
    }
}
