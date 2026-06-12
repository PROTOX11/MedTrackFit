package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.EmailService;
import com.example.medtrackfit.services.OtpService;
import com.example.medtrackfit.services.RoleBasedUserService;
import com.example.medtrackfit.services.UniversalUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private RoleBasedUserService roleBasedUserService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String email) {
        logger.info("Received request to send OTP to: {}", email);
        Map<String, String> response = new HashMap<>();
        try {
            String otp = otpService.generateAndStoreOtp(email);
            logger.info("OTP generated, sending email via Brevo to: {}", email);
            emailService.sendOtpEmail(email, otp);
            
            logger.info("OTP sent successfully to: {}", email);
            response.put("message", "OTP sent to your email.");
            response.put("email", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send OTP to {}: {}", email, e.getMessage(), e);
            response.put("error", "Failed to send OTP email. Please try again later.");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailRegistered(@RequestParam String email) {
        logger.info("Checking if email is already registered: {}", email);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean registered = universalUserService.isUserExistByEmail(email);
            response.put("registered", registered);
            if (registered) {
                String role = universalUserService.getUserRole(email);
                response.put("role", role);
                logger.info("Email {} is already registered as role: {}", email, role);
            } else {
                logger.info("Email {} is not registered.", email);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error checking email {}: {}", email, e.getMessage(), e);
            response.put("registered", false);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody VerifyRequest request) {
        logger.info("Received request to verify OTP for: {}", request.getEmail());
        Map<String, String> response = new HashMap<>();
        try {
            boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());
            if (isValid) {
                logger.info("OTP verification succeeded for: {}", request.getEmail());
                response.put("message", "OTP verified successfully.");
                response.put("email", request.getEmail());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("OTP verification failed (invalid or expired) for: {}", request.getEmail());
                response.put("error", "Invalid or expired OTP.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            logger.error("Failed to verify OTP for {}: {}", request.getEmail(), e.getMessage(), e);
            response.put("error", "Failed to verify OTP. Please try again later.");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-login")
    public ResponseEntity<Map<String, String>> verifyLoginOtp(@RequestParam String email, @RequestParam String otp, HttpSession session) {
        logger.info("Received request to verify login OTP for: {}", email);
        Map<String, String> response = new HashMap<>();
        try {
            boolean isValid = otpService.validateOtp(email, otp);

            if (!isValid) {
                logger.warn("Login OTP verification failed (invalid or expired) for: {}", email);
                response.put("error", "Invalid or expired OTP");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check if user exists
            if (!universalUserService.isUserExistByEmail(email)) {
                logger.warn("Login OTP verification succeeded, but user not found for: {}", email);
                response.put("error", "User not found. Please create an account first.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            UserDetails userDetails = universalUserService.getUserByEmail(email);
            
            // Auto-login
            logger.info("Authenticating user session for: {}", email);
            authenticateUser(userDetails, session);

            response.put("message", "Login successful");
            response.put("email", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to login OTP for {}: {}", email, e.getMessage(), e);
            response.put("error", "Failed to verify login OTP. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-signup")
    public ResponseEntity<Map<String, String>> verifySignupOtp(@RequestBody SignupRequest request, HttpSession session) {
        logger.info("Received request to verify signup OTP for: {}", request.getEmail());
        Map<String, String> response = new HashMap<>();
        try {
            boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());

            if (!isValid) {
                logger.warn("Signup OTP verification failed (invalid or expired) for: {}", request.getEmail());
                response.put("error", "Invalid or expired OTP");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check if user already exists
            if (universalUserService.isUserExistByEmail(request.getEmail())) {
                logger.warn("Signup OTP verification succeeded, but email already registered: {}", request.getEmail());
                response.put("error", "Email already registered. Please log in.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            String defaultProfilePicture = "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";

            // Save new user
            logger.info("Registering new user with role: {}, email: {}", request.getRole(), request.getEmail());
            UserDetails savedUser = roleBasedUserService.saveUserByRole(
                    request.getRole(),
                    request.getName(),
                    request.getEmail(),
                    "NO_PASSWORD", // Password not required for OTP
                    request.getPhoneNumber(),
                    request.getAbout(),
                    defaultProfilePicture
            );

            // Auto-login
            logger.info("Authenticating session for new user: {}", request.getEmail());
            authenticateUser(savedUser, session);

            response.put("message", "Account created successfully");
            response.put("email", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to signup OTP for {}: {}", request.getEmail(), e.getMessage(), e);
            response.put("error", "Failed to verify signup OTP. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private void authenticateUser(UserDetails userDetails, HttpSession session) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("loggedInUser", userDetails);
    }

    public static class VerifyRequest {
        private String email;
        private String otp;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }

    public static class SignupRequest {
        private String name;
        private String email;
        private String phoneNumber;
        private String role;
        private String about;
        private String otp;

        // Getters and Setters
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
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }
}
