package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.RoleBasedUserService;
import com.medtrackfit.forms.UserForm;
import com.medtrackfit.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.medtrackfit.helper.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private RoleBasedUserService roleBasedUserService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
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
        userForm.setRole("");
        userForm.setEmail("");
        userForm.setPhoneNumber("");
        userForm.setAbout("");
        model.addAttribute("userForm", userForm);
        System.out.println("Signup page loading");
        return "signup";
    }
    @RequestMapping(value = "/do-signup", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session, Model model) {
        System.out.println("Registration page loading");
        System.out.println(userForm);

        if (rBindingResult.hasErrors()) {
            return "signup";
        }

        String defaultProfilePicture = "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";

        try {
            // Check if user already exists in any role-based collection
            if (roleBasedUserService.isUserExistByRoleAndEmail(userForm.getRole(), userForm.getEmail())) {
                Message message = Message.builder().content("Email already exists for this role. Please use a different email or try logging in.").type(MessageType.red).build();
                session.setAttribute("message", message);
                model.addAttribute("userForm", userForm);
                return "signup";
            }

            // Save user to appropriate collection based on role
            UserDetails savedUserDetails = roleBasedUserService.saveUserByRole(
                userForm.getRole(),
                userForm.getName(),
                userForm.getEmail(),
                userForm.getPassword(),
                userForm.getPhoneNumber(),
                userForm.getAbout(),
                defaultProfilePicture
            );

            System.out.println("User saved to " + userForm.getRole() + " collection: " + userForm.getEmail());

            // Automatically log in the user after successful registration
            try {
                System.out.println("User authorities: " + savedUserDetails.getAuthorities());
                
                // Store user info in session for authentication
                session.setAttribute("loggedInUser", savedUserDetails);
                session.setAttribute("userEmail", savedUserDetails.getUsername());
                session.setAttribute("userRole", userForm.getRole());
                session.setAttribute("userAuthorities", savedUserDetails.getAuthorities());
                
                // Create authentication token with user details (using constructor with authorities)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    savedUserDetails.getUsername(), // Use email as principal
                    null, // No credentials needed since we just created the user
                    savedUserDetails.getAuthorities() // Authorities passed to constructor
                );
                
                // Set in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                System.out.println("User automatically logged in successfully: " + userForm.getEmail());
                System.out.println("Authentication object: " + authentication);
                System.out.println("Is authenticated: " + authentication.isAuthenticated());
                
                // Store authentication in session to persist across redirects
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                
                    // Forward to role-specific dashboard instead of redirect to maintain authentication context
                    return "forward:/" + userForm.getRole().toLowerCase() + "/dashboard";
            } catch (Exception authException) {
                System.out.println("Auto-login failed: " + authException.getMessage());
                authException.printStackTrace(); // Add stack trace for debugging
                
                // If auto-login fails, show success message and redirect to login
                Message message = Message.builder().content("Registration successful! Please log in with your credentials.").type(MessageType.green).build();
                session.setAttribute("message", message);
                return "redirect:/login";
            }
        } catch (IllegalStateException e) {
            // Handle duplicate email error
            System.out.println("Duplicate email error: " + e.getMessage());
            Message message = Message.builder().content("Email already exists. Please use a different email or try logging in.").type(MessageType.red).build();
            session.setAttribute("message", message);
            
            // Re-populate the form with user data
            model.addAttribute("userForm", userForm);
            return "signup";
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.out.println("Unexpected error during registration: " + e.getMessage());
            e.printStackTrace();
            Message message = Message.builder().content("Registration failed. Please try again.").type(MessageType.red).build();
            session.setAttribute("message", message);
            
            // Re-populate the form with user data
            model.addAttribute("userForm", userForm);
            return "signup";
        }
    }
}