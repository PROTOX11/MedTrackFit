package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.RecoveredPatient;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.HealthMentorService;
import com.example.medtrackfit.services.RecoveredPatientService;
import com.example.medtrackfit.services.SufferingPatientService;
import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HealthMentorService healthMentorService;

    @Autowired
    private RecoveredPatientService recoveredPatientService;

    @Autowired
    private SufferingPatientService sufferingPatientService;

    @Autowired
    private com.example.medtrackfit.repositories.SufferingPatientRepository sufferingPatientRepository;

    @GetMapping
    public ResponseEntity<?> getConnections(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String email = Helper.getEmailOfLoggedInUser(authentication);
        String role = universalUserService.getUserRole(email);
        String currentUserId = universalUserService.getUserId(email);

        List<Map<String, Object>> list = new ArrayList<>();

        if ("SufferingPatient".equals(role)) {
            SufferingPatient currentPatient = sufferingPatientService.getSufferingPatientByEmail(email);
            List<String> friends = currentPatient.getConnectedFriends();
            if (friends == null) friends = new ArrayList<>();

            // Add Doctors
            for (Doctor doc : doctorService.getAllDoctors()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", doc.getDoctorId());
                map.put("name", doc.getName());
                map.put("email", doc.getEmail());
                map.put("role", "Doctor");
                map.put("profilePicture", doc.getProfilePicture());
                map.put("status", doc.getSpecialization());
                map.put("connected", friends.contains(doc.getDoctorId()));
                list.add(map);
            }

            // Add Mentors
            for (HealthMentor mentor : healthMentorService.getAllHealthMentors()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", mentor.getMentorId());
                map.put("name", mentor.getName());
                map.put("email", mentor.getEmail());
                map.put("role", "HealthMentor");
                map.put("profilePicture", mentor.getProfilePicture());
                map.put("status", mentor.getAreaOfExpertise());
                map.put("connected", friends.contains(mentor.getMentorId()));
                list.add(map);
            }

            // Add Recovered Patients
            for (RecoveredPatient rec : recoveredPatientService.getAllRecoveredPatients()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rec.getPatientId());
                map.put("name", rec.getName());
                map.put("email", rec.getEmail());
                map.put("role", "RecoveredPatient");
                map.put("profilePicture", rec.getProfilePicture());
                map.put("status", "Wellness Champion");
                map.put("connected", friends.contains(rec.getPatientId()));
                list.add(map);
            }

        } else {
            // Doctors, Mentors, Recovered Patients see SufferingPatients who connected with them
            for (SufferingPatient pat : sufferingPatientService.getAllSufferingPatients()) {
                List<String> friends = pat.getConnectedFriends();
                boolean isConnected = (friends != null && friends.contains(currentUserId));
                
                // For Doctors, they also connect if doctorId is set
                if ("Doctor".equals(role) && pat.getDoctorId() != null && pat.getDoctorId().equals(currentUserId)) {
                    isConnected = true;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("id", pat.getPatientId());
                map.put("name", pat.getName());
                map.put("email", pat.getEmail());
                map.put("role", "SufferingPatient");
                map.put("profilePicture", pat.getProfilePicture());
                map.put("status", pat.getMedicalCondition());
                map.put("connected", isConnected);
                list.add(map);
            }
        }

        return ResponseEntity.ok(list);
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleConnection(@RequestBody Map<String, String> body,
                                             Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String email = Helper.getEmailOfLoggedInUser(authentication);
        String role = universalUserService.getUserRole(email);

        String targetId = body.get("targetId");
        if (targetId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Target ID is required"));
        }

        if ("SufferingPatient".equals(role)) {
            SufferingPatient currentPatient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (currentPatient.getConnectedFriends() == null) {
                currentPatient.setConnectedFriends(new ArrayList<>());
            }

            List<String> friends = currentPatient.getConnectedFriends();
            boolean isConnecting = false;
            if (friends.contains(targetId)) {
                friends.remove(targetId);
            } else {
                friends.add(targetId);
                isConnecting = true;
            }
            sufferingPatientRepository.save(currentPatient);
            return ResponseEntity.ok(Map.of("success", true, "connected", isConnecting));
        } else {
            // Other roles toggle the connection by modifying the SufferingPatient's list
            // Target is a SufferingPatient
            SufferingPatient pat = sufferingPatientService.getSufferingPatientById(targetId);
            if (pat != null) {
                String currentUserId = universalUserService.getUserId(email);
                if (pat.getConnectedFriends() == null) {
                    pat.setConnectedFriends(new ArrayList<>());
                }
                List<String> friends = pat.getConnectedFriends();
                boolean isConnecting = false;
                if (friends.contains(currentUserId)) {
                    friends.remove(currentUserId);
                } else {
                    friends.add(currentUserId);
                    isConnecting = true;
                }
                sufferingPatientRepository.save(pat);
                return ResponseEntity.ok(Map.of("success", true, "connected", isConnecting));
            }
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid connection request"));
    }
}
