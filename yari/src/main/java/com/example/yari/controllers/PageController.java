package com.example.yari.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PageController {
    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("youtube_channel","drink the code");
        model.addAttribute("github","https://github.com/PROTOX11");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutpage(Model model) {
        model.addAttribute("islogin", true);
        System.out.println("About page loading");
        return "about";
    }
    
    @RequestMapping("/services")
    public String servicespage() {
        System.out.println("Services page loading");
        return "services";
    }
    @RequestMapping("/login")
    public String login() {
        System.out.println("login page loading");
        return "login";
    }
    @RequestMapping("/signup")
    public String signup() {
        System.out.println("Signup page loading");
        return "signup";
    }
    @RequestMapping("/contact")
    public String contact() {
        System.out.println("Signup page loading");
        return "contact";
}
}