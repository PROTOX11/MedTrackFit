package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.RecoveredPatient;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MeController {

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private com.example.medtrackfit.services.UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String email = Helper.getEmailOfLoggedInUser(authentication);
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        UserDetails userDetails = universalUserService.getUserByEmail(email);
        String role = universalUserService.getUserRole(email);
        String id = universalUserService.getUserId(email);
        String name = universalUserService.getUserName(email);
        String profilePicture = null;
        String about = null;
        String phoneNumber = null;

        if (userDetails == null) {
            com.example.medtrackfit.entities.User genericUser = userService.getUserByEmail(email);
            if (genericUser != null) {
                name = genericUser.getName();
                profilePicture = genericUser.getProfilePicture();
                about = genericUser.getAbout();
                phoneNumber = genericUser.getPhoneNumber();
                id = genericUser.getUserId();
                role = "USER";
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
        } else {
            if (userDetails instanceof Doctor) {
                Doctor doc = (Doctor) userDetails;
                profilePicture = doc.getProfilePicture();
                about = doc.getSpecialization();
                phoneNumber = doc.getPhoneNumber();
            } else if (userDetails instanceof HealthMentor) {
                HealthMentor mentor = (HealthMentor) userDetails;
                profilePicture = mentor.getProfilePicture();
                about = mentor.getAreaOfExpertise();
                phoneNumber = mentor.getPhoneNumber();
            } else if (userDetails instanceof SufferingPatient) {
                SufferingPatient patient = (SufferingPatient) userDetails;
                profilePicture = patient.getProfilePicture();
                about = patient.getMedicalCondition();
                phoneNumber = patient.getPhoneNumber();
            } else if (userDetails instanceof RecoveredPatient) {
                RecoveredPatient patient = (RecoveredPatient) userDetails;
                profilePicture = patient.getProfilePicture();
                about = patient.getAbout();
                phoneNumber = patient.getPhoneNumber();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("email", email);
        response.put("name", name);
        response.put("role", role);
        response.put("profilePicture", profilePicture);
        response.put("about", about);
        response.put("phoneNumber", phoneNumber);

        return ResponseEntity.ok(response);
    }
}
