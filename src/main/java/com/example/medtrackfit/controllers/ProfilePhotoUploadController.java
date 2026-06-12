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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
public class ProfilePhotoUploadController {

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HealthMentorService healthMentorService;

    @Autowired
    private SufferingPatientService sufferingPatientService;

    @Autowired
    private RecoveredPatientService recoveredPatientService;

    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication == null) {
            response.put("success", false);
            response.put("message", "User not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "File is empty");
            return ResponseEntity.badRequest().body(response);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            response.put("success", false);
            response.put("message", "Only image files are allowed");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            Object user = universalUserService.getUserByEmail(email);

            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }

            // Create directories if they do not exist
            String uploadDir = "src/main/resources/static/uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Also target the compiled directory so it is visible immediately without recompilation
            String targetDir = "target/classes/static/uploads/";
            File targetFolder = new File(targetDir);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            Path uploadPath = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

            // Also copy to classes/static/uploads
            try {
                Path targetPath = Paths.get(targetDir + filename);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignored) {
            }

            String relativeUrl = "/uploads/" + filename;

            if (user instanceof Doctor) {
                Doctor d = (Doctor) user;
                d.setProfilePicture(relativeUrl);
                doctorService.saveDoctor(d);
            } else if (user instanceof HealthMentor) {
                HealthMentor m = (HealthMentor) user;
                m.setProfilePicture(relativeUrl);
                healthMentorService.saveHealthMentor(m);
            } else if (user instanceof SufferingPatient) {
                SufferingPatient sp = (SufferingPatient) user;
                sp.setProfilePicture(relativeUrl);
                sufferingPatientService.saveSufferingPatient(sp);
            } else if (user instanceof RecoveredPatient) {
                RecoveredPatient rp = (RecoveredPatient) user;
                rp.setProfilePicture(relativeUrl);
                recoveredPatientService.saveRecoveredPatient(rp);
            }

            response.put("success", true);
            response.put("profilePicture", relativeUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save photo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
