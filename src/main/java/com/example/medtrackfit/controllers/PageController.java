package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.services.UserService;
import com.medtrackfit.forms.UserForm;
import com.medtrackfit.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.medtrackfit.helper.MessageType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

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
    public String login(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("Login page loading");
        return "login";
    }
    @RequestMapping("/privacy")
    public String privacy() {
        System.out.println("Privacy page loading");
        return "privacy";
    }
    @RequestMapping("/logged_home")

    public String logged_home() {
        System.out.println("Logged home page loading");
        return "logged_home";
    }

    @RequestMapping("/contact")
    public String contact() {
    System.out.println("Signup page loading");
    return "contact";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        UserForm userForm = new UserForm();
        userForm.setName("");
        userForm.setEmail("");
        userForm.setPhoneNumber("91");
        userForm.setAbout("");
        model.addAttribute("userForm", userForm);
        System.out.println("Signup page loading");
        return "signup";
    }
    @RequestMapping(value = "/do-signup", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {
        System.out.println("Registration page loading");
        System.out.println(userForm);

        if (rBindingResult.hasErrors()) {
            return "signup";
        }
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePicture("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=");
        


        User savedUser = userService.saveUser(user);
        System.out.println("user saved :");

        Message message = Message.builder().content("Registration successfull").type(MessageType.green).build();
        session.setAttribute("message", message);
        return "redirect:/signup";
    }
}