package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.EmailService;
import com.example.medtrackfit.services.OtpService;
import com.example.medtrackfit.services.UniversalUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private com.example.medtrackfit.services.RoleBasedUserService roleBasedUserService;

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        logger.info("Received request to send OTP for login: {}", email);
        Map<String, String> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("message", "Email is required.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Check if user exists
            if (!universalUserService.isUserExistByEmail(email)) {
                logger.warn("Send OTP failed: User not found for email: {}", email);
                response.put("message", "User not found. Please sign up first.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            String otp = otpService.generateAndStoreOtp(email);
            logger.info("OTP generated, sending login email to: {}", email);
            emailService.sendOtpEmail(email, otp);

            response.put("message", "OTP sent successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send OTP to {}: {}", email, e.getMessage(), e);
            response.put("message", "Failed to send OTP email. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody SignupRequest request, HttpSession session) {
        String email = request.getEmail();
        logger.info("Received request to register new user: {}", email);
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("message", "Email is required.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Check if user already exists
            if (universalUserService.isUserExistByEmail(email)) {
                logger.warn("Register failed: User already exists for email: {}", email);
                response.put("alreadyExists", true);
                response.put("message", "Email is already registered.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Generate OTP
            String otp = otpService.generateAndStoreOtp(email);
            logger.info("OTP generated for registration, sending email to: {}", email);
            emailService.sendOtpEmail(email, otp);

            // Store signup request in session for verification phase
            session.setAttribute("pendingSignup_" + email, request);

            response.put("message", "OTP sent successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send signup OTP to {}: {}", email, e.getMessage(), e);
            response.put("message", "Failed to send OTP email. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/oauth-complete")
    public ResponseEntity<?> oauthComplete(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("email");
        String name = body.get("name");
        String phoneNumber = body.get("phoneNumber");
        String about = body.get("about");
        String role = body.get("role");
        String picture = body.get("picture");

        Map<String, Object> response = new HashMap<>();

        if (email == null || name == null || role == null) {
            response.put("message", "Email, Name, and Role are required.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Check if user already exists in role-based DB
            if (universalUserService.isUserExistByEmail(email)) {
                response.put("message", "User already registered.");
                return ResponseEntity.badRequest().body(response);
            }

            // Normalize role
            String normalizedRole = role.toLowerCase().replace("_", "");
            if (normalizedRole.equals("user")) {
                normalizedRole = "sufferingpatient";
            }

            String profilePic = (picture != null && !picture.trim().isEmpty()) ? picture : "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";

            org.springframework.security.core.userdetails.UserDetails userDetails = roleBasedUserService.saveUserByRole(
                    normalizedRole,
                    name,
                    email,
                    "NO_PASSWORD",
                    phoneNumber != null ? phoneNumber : "",
                    about != null ? about : "",
                    profilePic
            );

            // Log the user in programmatically
            org.springframework.security.authentication.UsernamePasswordAuthenticationToken authenticationToken =
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()
                    );
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            session.setAttribute("SPRING_SECURITY_CONTEXT", org.springframework.security.core.context.SecurityContextHolder.getContext());
            session.setAttribute("loggedInUser", userDetails);

            response.put("success", true);
            response.put("message", "OAuth Registration complete.");
            
            // Return role prefix path for dashboard redirection
            String prefix;
            switch (normalizedRole) {
                case "doctor":
                    prefix = "/doctor";
                    break;
                case "healthmentor":
                case "mentor":
                    prefix = "/mentor";
                    break;
                case "recoveredpatient":
                    prefix = "/recoveredpatient";
                    break;
                case "sufferingpatient":
                default:
                    prefix = "/suff-pat";
                    break;
            }
            response.put("redirectUrl", prefix + "/dashboard");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to complete OAuth registration for {}: {}", email, e.getMessage(), e);
            response.put("message", "Failed to complete registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public static class OtpRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class SignupRequest implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private String email;
        private String phoneNumber;
        private String role;
        private String about;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getAbout() { return about; }
        public void setAbout(String about) { this.about = about; }
    }
}
