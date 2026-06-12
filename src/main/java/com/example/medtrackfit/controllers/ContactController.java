package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/api/contact")
    public ResponseEntity<Map<String, Object>> submitContactForm(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String subject = body.get("subject");
        String message = body.get("message");
        logger.info("Contact form submission received from: {}", email);
        Map<String, Object> response = new HashMap<>();
        try {
            emailService.sendContactEmail(name, email, subject, message);
            response.put("success", true);
            response.put("message", "Message sent successfully! We will contact you soon.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send contact form email: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to send message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
