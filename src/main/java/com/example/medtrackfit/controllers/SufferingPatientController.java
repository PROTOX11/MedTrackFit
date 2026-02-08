package com.example.medtrackfit.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.medtrackfit.services.AllBlogPostService;
import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.UniversalUserService;
import com.example.medtrackfit.services.HealthMentorService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.RecoveredPatient;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.entities.PatientPerformance;
import com.example.medtrackfit.services.SufferingPatientService;
import com.medtrackfit.helper.Helper;

@Controller
@RequestMapping("/suff-pat")
public class SufferingPatientController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private AllBlogPostService allBlogPostService;

    @Autowired
    private HealthMentorService healthMentorService;

    // @Autowired
    // private RecoveredPatientService recoveredPatientService;

    @Autowired
    private SufferingPatientService sufferingPatientService;

    @Autowired
    private com.example.medtrackfit.repositories.SufferingPatientRepository sufferingPatientRepository;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            model.addAttribute("userRole", userRole);
            model.addAttribute("loggedInUser", universalUserService.getUserByEmail(username));
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "suff-pat/dashboard";
    }

    // Health metrics endpoints for suffering patients
    @GetMapping("/performance/{patientId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> getPatientPerformance(@PathVariable String patientId) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            SufferingPatient patient = sufferingPatientService.getSufferingPatientById(patientId);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            PatientPerformance perf = patient.getPatientPerformance();
            if (perf == null) {
                perf = PatientPerformance.builder().patientId(patient.getPatientId()).healthScore(0).treatmentProgress(0).sessionsAttended(0).medicationAdherence(0.0).overallImprovement(0.0).meditationScore(0).breatheScore(0).hydrationScore(0).nutritionScore(0).build();
            }
            response.put("success", true);
            response.put("meditationScore", perf.getMeditationScore());
            response.put("breatheScore", perf.getBreatheScore());
            response.put("hydrationScore", perf.getHydrationScore());
            response.put("nutritionScore", perf.getNutritionScore());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/{patientId}/meditation")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> updateMeditation(@PathVariable String patientId,
                                                                                                   @RequestBody java.util.Map<String, Object> req) {
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        try {
            Integer meditationTime = (Integer) (req.get("meditationTime") instanceof Integer ? req.get("meditationTime") : ((Number) req.get("meditationTime")).intValue());
            SufferingPatient patient = sufferingPatientService.getSufferingPatientById(patientId);
            if (patient == null) {
                resp.put("success", false);
                resp.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(resp);
            }
            PatientPerformance perf = patient.getPatientPerformance();
            if (perf == null) {
                perf = PatientPerformance.builder().patientId(patient.getPatientId()).healthScore(0).treatmentProgress(0).sessionsAttended(0).medicationAdherence(0.0).overallImprovement(0.0).meditationScore(0).breatheScore(0).hydrationScore(0).nutritionScore(0).build();
                patient.setPatientPerformance(perf);
            }
            int current = perf.getMeditationScore() == null ? 0 : perf.getMeditationScore();
            perf.setMeditationScore(current + meditationTime);
            sufferingPatientRepository.save(patient);
            resp.put("success", true);
            resp.put("meditationScore", perf.getMeditationScore());
            return org.springframework.http.ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(resp);
        }
    }

    @PostMapping("/{patientId}/breatheScore")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> updateBreathe(@PathVariable String patientId,
                                                                                                 @RequestBody java.util.Map<String, Object> req) {
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        try {
            Integer breatheScore = (Integer) (req.get("breatheScore") instanceof Integer ? req.get("breatheScore") : ((Number) req.get("breatheScore")).intValue());
            SufferingPatient patient = sufferingPatientService.getSufferingPatientById(patientId);
            if (patient == null) {
                resp.put("success", false);
                resp.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(resp);
            }
            PatientPerformance perf = patient.getPatientPerformance();
            if (perf == null) {
                perf = PatientPerformance.builder().patientId(patient.getPatientId()).healthScore(0).treatmentProgress(0).sessionsAttended(0).medicationAdherence(0.0).overallImprovement(0.0).meditationScore(0).breatheScore(0).hydrationScore(0).nutritionScore(0).build();
                patient.setPatientPerformance(perf);
            }
            perf.setBreatheScore(breatheScore);
            sufferingPatientRepository.save(patient);
            resp.put("success", true);
            resp.put("breatheScore", perf.getBreatheScore());
            return org.springframework.http.ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(resp);
        }
    }

    @PostMapping("/{patientId}/hydration")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> updateHydration(@PathVariable String patientId,
                                                                                                  @RequestBody java.util.Map<String, Object> req) {
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        try {
            Integer hydrationAmount = (Integer) (req.get("hydrationAmount") instanceof Integer ? req.get("hydrationAmount") : ((Number) req.get("hydrationAmount")).intValue());
            SufferingPatient patient = sufferingPatientService.getSufferingPatientById(patientId);
            if (patient == null) {
                resp.put("success", false);
                resp.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(resp);
            }
            PatientPerformance perf = patient.getPatientPerformance();
            if (perf == null) {
                perf = PatientPerformance.builder().patientId(patient.getPatientId()).healthScore(0).treatmentProgress(0).sessionsAttended(0).medicationAdherence(0.0).overallImprovement(0.0).meditationScore(0).breatheScore(0).hydrationScore(0).nutritionScore(0).build();
                patient.setPatientPerformance(perf);
            }
            int current = perf.getHydrationScore() == null ? 0 : perf.getHydrationScore();
            perf.setHydrationScore(current + hydrationAmount);
            sufferingPatientRepository.save(patient);
            resp.put("success", true);
            resp.put("hydrationScore", perf.getHydrationScore());
            return org.springframework.http.ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(resp);
        }
    }

    @PostMapping("/food/save")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> saveFoodEntriesForPatient(@RequestBody java.util.List<java.util.Map<String, Object>> foodItems,
                                                                                                              Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            String username = com.medtrackfit.helper.Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = (SufferingPatient) universalUserService.getUserByEmail(username);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }

            // Simple nutrition scoring logic similar to UserController
            double totalNutritionScore = 0;
            for (java.util.Map<String, Object> entry : foodItems) {
                double calories = ((Number) entry.getOrDefault("calories", 0)).doubleValue();
                double protein = ((Number) entry.getOrDefault("protein", 0)).doubleValue();
                double fiber = ((Number) entry.getOrDefault("fiber", 0)).doubleValue();
                double vitamins = ((Number) entry.getOrDefault("vitamins", 0)).doubleValue();

                double score = (calories * 0.3 + protein * 0.3 + fiber * 0.2 + vitamins * 0.2) / 100;
                totalNutritionScore += Math.min(10, score);
            }
            int score = Math.min(10, (int) Math.round(totalNutritionScore));

            PatientPerformance perf = patient.getPatientPerformance();
            if (perf == null) {
                perf = PatientPerformance.builder().patientId(patient.getPatientId()).healthScore(0).treatmentProgress(0).sessionsAttended(0).medicationAdherence(0.0).overallImprovement(0.0).meditationScore(0).breatheScore(0).hydrationScore(0).nutritionScore(score).build();
                patient.setPatientPerformance(perf);
            } else {
                int current = perf.getNutritionScore() == null ? 0 : perf.getNutritionScore();
                perf.setNutritionScore(current + score);
            }
            sufferingPatientRepository.save(patient);

            response.put("success", true);
            response.put("nutritionScore", perf.getNutritionScore());
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/food/search")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.List<java.util.Map<String, Object>>> searchFoodsForPatient(@RequestParam String query) {
        java.util.List<java.util.Map<String, Object>> foodResults = new java.util.ArrayList<>();
        try {
            if (query != null && !query.trim().isEmpty()) {
                String lowerQuery = query.toLowerCase();
                if (lowerQuery.contains("apple")) {
                    java.util.Map<String, Object> apple = new java.util.HashMap<>();
                    apple.put("description", "Apple, raw, with skin");
                    apple.put("calories", 52.0);
                    apple.put("protein", 0.2);
                    apple.put("fiber", 2.4);
                    apple.put("vitamins", 0.5);
                    apple.put("sugar", 10.4);
                    apple.put("saturatedFat", 0.1);
                    apple.put("sodium", 1.0);
                    apple.put("minerals", 0.2);
                    apple.put("transFat", false);
                    apple.put("unit", "pieces");
                    apple.put("fdcId", "apple_1");
                    foodResults.add(apple);
                }
                if (lowerQuery.contains("banana")) {
                    java.util.Map<String, Object> banana = new java.util.HashMap<>();
                    banana.put("description", "Banana, raw");
                    banana.put("calories", 89.0);
                    banana.put("protein", 1.1);
                    banana.put("fiber", 2.6);
                    banana.put("vitamins", 0.7);
                    banana.put("sugar", 12.2);
                    banana.put("saturatedFat", 0.1);
                    banana.put("sodium", 1.0);
                    banana.put("minerals", 0.3);
                    banana.put("transFat", false);
                    banana.put("unit", "pieces");
                    banana.put("fdcId", "banana_1");
                    foodResults.add(banana);
                }
            }
            return org.springframework.http.ResponseEntity.ok(foodResults);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.internalServerError().body(foodResults);
        }
    }

    @GetMapping("/profile")
    public String profile() {
        return "suff-pat/profile";
    }

    @GetMapping("/connect_recovered")
    public String connectRecovered(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient != null) {
                model.addAttribute("loggedInUser", patient);
            }
        }
        return "suff-pat/connect_recovered";
    }

    @GetMapping("/connect_recovered_patient/request/{patientId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> requestConnectRecoveredPatient(@PathVariable String patientId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            if (!patient.getConnectedFriends().contains(patientId)) {
                patient.getConnectedFriends().add(patientId);
                sufferingPatientRepository.save(patient);
            }
            response.put("success", true);
            response.put("message", "Connected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/disconnect_recovered/{patientId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> disconnectRecovered(@PathVariable String patientId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            patient.getConnectedFriends().remove(patientId);
            sufferingPatientRepository.save(patient);
            response.put("success", true);
            response.put("message", "Disconnected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/connect_doctor")
    public String connectDoctor(Model model, Authentication authentication) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        
        if (authentication != null) {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient != null) {
                model.addAttribute("loggedInUser", patient);
            }
        }
        return "suff-pat/connect_doctor";
    }

    @GetMapping("/connect_mentor")
    public String connectMentor(Model model, Authentication authentication) {
        List<HealthMentor> mentors = healthMentorService.getAllHealthMentors();
        model.addAttribute("mentors", mentors);
        
        if (authentication != null) {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient != null) {
                model.addAttribute("loggedInUser", patient);
            }
        }
        return "suff-pat/connect_mentor";
    }

    @GetMapping("/connect_mentor/request/{mentorId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> requestConnectMentor(@PathVariable String mentorId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            if (!patient.getConnectedFriends().contains(mentorId)) {
                patient.getConnectedFriends().add(mentorId);
                sufferingPatientRepository.save(patient);
            }
            response.put("success", true);
            response.put("message", "Connected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/disconnect_mentor/{mentorId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> disconnectMentor(@PathVariable String mentorId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            patient.getConnectedFriends().remove(mentorId);
            sufferingPatientRepository.save(patient);
            response.put("success", true);
            response.put("message", "Disconnected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/connect_doctor/request/{doctorId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> requestConnectDoctor(@PathVariable String doctorId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            if (!patient.getConnectedFriends().contains(doctorId)) {
                patient.getConnectedFriends().add(doctorId);
                sufferingPatientRepository.save(patient);
            }
            response.put("success", true);
            response.put("message", "Connected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/disconnect_doctor/{doctorId}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> disconnectDoctor(@PathVariable String doctorId, Authentication authentication) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            SufferingPatient patient = sufferingPatientService.getSufferingPatientByEmail(email);
            if (patient == null) {
                response.put("success", false);
                response.put("message", "Patient not found");
                return org.springframework.http.ResponseEntity.badRequest().body(response);
            }
            patient.getConnectedFriends().remove(doctorId);
            sufferingPatientRepository.save(patient);
            response.put("success", true);
            response.put("message", "Disconnected successfully");
            return org.springframework.http.ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return org.springframework.http.ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"SufferingPatient".equals(userRole)) {
                return "redirect:/user/dashboard";
            }
        }

        return "suff-pat/edit-profile";
    }

    @PostMapping("/update-profile")
    public String updateSufferingPatientProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "medicalCondition", required = false) String medicalCondition,
            @RequestParam(value = "currentSymptoms", required = false) String currentSymptoms,
            @RequestParam(value = "treatmentDuration", required = false) String treatmentDuration,
            @RequestParam(value = "emergencyContact", required = false) String emergencyContact,
            @RequestParam(value = "preferredMentorType", required = false) String preferredMentorType,
            @RequestParam(value = "about", required = false) String about,
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);

        if (!"SufferingPatient".equals(userRole)) {
            return "redirect:/user/dashboard";
        }

        SufferingPatient patient = (SufferingPatient) universalUserService.getUserByEmail(username);

        if (patient == null) {
            model.addAttribute("error", "Patient not found");
            return "suff-pat/edit-profile";
        }

        try {
            // Update basic information
            patient.setName(name);
            patient.setEmail(email);
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                patient.setPhoneNumber(phoneNumber);
            }
            if (medicalCondition != null && !medicalCondition.trim().isEmpty()) {
                patient.setMedicalCondition(medicalCondition);
            }
            if (currentSymptoms != null && !currentSymptoms.trim().isEmpty()) {
                patient.setCurrentSymptoms(currentSymptoms);
            }
            if (treatmentDuration != null && !treatmentDuration.trim().isEmpty()) {
                patient.setTreatmentDuration(treatmentDuration);
            }
            if (emergencyContact != null && !emergencyContact.trim().isEmpty()) {
                patient.setEmergencyContact(emergencyContact);
            }
            if (preferredMentorType != null && !preferredMentorType.trim().isEmpty()) {
                patient.setPreferredMentorType(preferredMentorType);
            }
            if (about != null && !about.trim().isEmpty()) {
                patient.setAbout(about);
            }

            // Save the updated patient
            sufferingPatientService.saveSufferingPatient(patient);

            return "redirect:/suff-pat/profile?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            model.addAttribute("loggedInUser", patient);
            return "suff-pat/edit-profile";
        }
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Authentication authentication,
            Model model) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        SufferingPatient patient = (SufferingPatient) universalUserService.getUserByEmail(username);

        if (patient == null) {
            model.addAttribute("error", "Patient not found");
            return "suff-pat/profile";
        }

        try {
            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed");
                    model.addAttribute("loggedInUser", patient);
                    return "suff-pat/profile";
                }

                // Note: Need to implement updateProfilePicture method in SufferingPatientService
                // For now, we'll skip this functionality
                model.addAttribute("error", "Profile picture update not yet implemented");
                model.addAttribute("loggedInUser", patient);
                return "suff-pat/profile";
            } else {
                model.addAttribute("error", "No file uploaded");
                model.addAttribute("loggedInUser", patient);
                return "suff-pat/profile";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile picture: " + e.getMessage());
            model.addAttribute("loggedInUser", patient);
            return "suff-pat/profile";
        }
    }

    @GetMapping("/blog")
    public String blog(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch all public blogs from all user types
        Pageable pageable = PageRequest.of(page, size);
        Page<AllBlogPost> allBlogs = allBlogPostService.findAllPublicBlogs(pageable);

        // Get blog statistics
        long totalPosts = allBlogPostService.countByStatus(AllBlogPost.PostStatus.PUBLISHED);
        long doctorPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.DOCTOR, AllBlogPost.PostStatus.PUBLISHED);
        long mentorPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.MENTOR, AllBlogPost.PostStatus.PUBLISHED);
        long patientPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.RECOVERED_PATIENT, AllBlogPost.PostStatus.PUBLISHED);

        model.addAttribute("allBlogs", allBlogs.getContent());
        model.addAttribute("currentPage", allBlogs.getNumber());
        model.addAttribute("totalPages", allBlogs.getTotalPages());
        model.addAttribute("totalElements", allBlogs.getTotalElements());
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("doctorPosts", doctorPosts);
        model.addAttribute("mentorPosts", mentorPosts);
        model.addAttribute("patientPosts", patientPosts);
        model.addAttribute("baseUrl", "/suff-pat/blog");

        return "suff-pat/blog";
    }
}
