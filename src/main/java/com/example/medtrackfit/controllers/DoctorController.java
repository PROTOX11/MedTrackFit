package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.UniversalUserService;
import com.example.medtrackfit.services.AllBlogPostService;
import com.example.medtrackfit.services.CloudinaryService;
import com.medtrackfit.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AllBlogPostService allBlogPostService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private com.example.medtrackfit.services.SufferingPatientService sufferingPatientService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            model.addAttribute("userRole", userRole);
            model.addAttribute("loggedInUser", universalUserService.getUserByEmail(username));
        }
    }

    @RequestMapping("/dashboard")
    public String doctorDashboard(Model model, Authentication authentication) {
        logger.info("Doctor dashboard accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor dashboard: {}", username);
                return "redirect:/";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
            logger.info("Doctor dashboard loaded for: {}", doctor.getName());
        }
        
        return "doctor/dashboard";
    }

    @RequestMapping("/chat")
    public String doctorChat(Model model, Authentication authentication) {
        logger.info("Doctor chat page accessed");
        
        if (authentication == null) {
            logger.warn("Unauthenticated user attempted to access doctor chat");
            return "redirect:/login";
        }
        
        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);
        
        if (!"Doctor".equals(userRole)) {
            logger.warn("Non-doctor user attempted to access doctor chat: {}", username);
            return "redirect:/";
        }
        
        Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
        model.addAttribute("doctor", doctor);
        model.addAttribute("userRole", userRole);
        
        // Add sample data for patients and mentors
        // In a real application, this would come from database
        model.addAttribute("patients", createSamplePatients());
        model.addAttribute("mentors", createSampleMentors());
        
        return "doctor/chat";
    }
    
    // Sample data methods - in real application, these would come from database
    private java.util.List<java.util.Map<String, Object>> createSamplePatients() {
        java.util.List<java.util.Map<String, Object>> patients = new java.util.ArrayList<>();
        
        java.util.Map<String, Object> patient1 = new java.util.HashMap<>();
        patient1.put("id", "patient1");
        patient1.put("name", "Sarah Johnson");
        patient1.put("mentorName", "Dr. Lisa Thompson");
        patient1.put("status", "Suffering from Diabetes");
        patients.add(patient1);
        
        java.util.Map<String, Object> patient2 = new java.util.HashMap<>();
        patient2.put("id", "patient2");
        patient2.put("name", "Michael Chen");
        patient2.put("mentorName", "Dr. James Wilson");
        patient2.put("status", "Recovering from Hypertension");
        patients.add(patient2);
        
        java.util.Map<String, Object> patient3 = new java.util.HashMap<>();
        patient3.put("id", "patient3");
        patient3.put("name", "Emily Rodriguez");
        patient3.put("mentorName", "Dr. Maria Garcia");
        patient3.put("status", "Post-surgery Recovery");
        patients.add(patient3);
        
        java.util.Map<String, Object> patient4 = new java.util.HashMap<>();
        patient4.put("id", "patient4");
        patient4.put("name", "David Wilson");
        patient4.put("mentorName", "Dr. Robert Kim");
        patient4.put("status", "Heart Disease Management");
        patients.add(patient4);
        
        java.util.Map<String, Object> patient5 = new java.util.HashMap<>();
        patient5.put("id", "patient5");
        patient5.put("name", "Lisa Brown");
        patient5.put("mentorName", "Dr. Sarah Davis");
        patient5.put("status", "Asthma Treatment");
        patients.add(patient5);
        
        return patients;
    }
    
    private java.util.List<java.util.Map<String, Object>> createSampleMentors() {
        java.util.List<java.util.Map<String, Object>> mentors = new java.util.ArrayList<>();
        
        java.util.Map<String, Object> mentor1 = new java.util.HashMap<>();
        mentor1.put("id", "mentor1");
        mentor1.put("name", "Dr. Lisa Thompson");
        mentor1.put("specialty", "Cardiology Mentor");
        mentor1.put("status", "online");
        mentor1.put("lastMessage", "Hi Dr., I have a patient with complex cardiac issues...");
        mentor1.put("time", "5 min ago");
        mentor1.put("avatarColor", "#3b82f6 0%, #1d4ed8 100%");
        mentors.add(mentor1);
        
        java.util.Map<String, Object> mentor2 = new java.util.HashMap<>();
        mentor2.put("id", "mentor2");
        mentor2.put("name", "Dr. James Wilson");
        mentor2.put("specialty", "Neurology Mentor");
        mentor2.put("status", "offline");
        mentor2.put("lastMessage", "The neurological assessment looks promising...");
        mentor2.put("time", "2 hours ago");
        mentor2.put("avatarColor", "#6366f1 0%, #4f46e5 100%");
        mentors.add(mentor2);
        
        java.util.Map<String, Object> mentor3 = new java.util.HashMap<>();
        mentor3.put("id", "mentor3");
        mentor3.put("name", "Dr. Maria Garcia");
        mentor3.put("specialty", "Pediatrics Mentor");
        mentor3.put("status", "online");
        mentor3.put("lastMessage", "I have an interesting pediatric case...");
        mentor3.put("time", "10 min ago");
        mentor3.put("avatarColor", "#ec4899 0%, #db2777 100%");
        mentors.add(mentor3);
        
        return mentors;
    }

    @RequestMapping("/blog")
    public String doctorBlog(Model model, Authentication authentication,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        logger.info("Doctor blog page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor blog: {}", username);
                return "redirect:/";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
            
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
        }
        
        return "doctor/blog";
    }

    @RequestMapping("/patients")
    public String doctorPatients(Model model, Authentication authentication) {
        logger.info("Doctor patients page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor patients: {}", username);
                return "redirect:/user/dashboard";
            }

            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);

            // Fetch suffering patients from database
            List<com.example.medtrackfit.entities.SufferingPatient> sufferingPatients = sufferingPatientService.getAllSufferingPatients();
            model.addAttribute("sufferingPatients", sufferingPatients);
        }

        return "doctor/patients";
    }

    @RequestMapping("/records")
    public String doctorRecords(Model model, Authentication authentication) {
        logger.info("Doctor records page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor records: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/records";
    }

    @RequestMapping("/prescriptions")
    public String doctorPrescriptions(Model model, Authentication authentication) {
        logger.info("Doctor prescriptions page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor prescriptions: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/prescriptions";
    }

    @RequestMapping("/emergency")
    public String doctorEmergency(Model model, Authentication authentication) {
        logger.info("Doctor emergency page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor emergency: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/emergency";
    }

    @RequestMapping("/profile")
    public String doctorProfile(Model model, Authentication authentication) {
        logger.info("Doctor profile page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor profile: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/profile";
    }

    @RequestMapping("/edit-profile")
    public String doctorEditProfile(Model model, Authentication authentication) {
        logger.info("Doctor edit profile page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor edit profile: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/edit-profile";
    }

    @PostMapping("/update-profile")
    public String updateDoctorProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "yearsOfExperience", required = false) String yearsOfExperience,
            @RequestParam(value = "qualification", required = false) String qualification,
            @RequestParam(value = "licenseNumber", required = false) String licenseNumber,
            @RequestParam(value = "about", required = false) String about,
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);

        if (!"Doctor".equals(userRole)) {
            logger.warn("Non-doctor user attempted to update doctor profile: {}", username);
            return "redirect:/user/dashboard";
        }

        Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);

        if (doctor == null) {
            model.addAttribute("error", "Doctor not found");
            return "error";
        }

        try {
            // Update basic information
            doctor.setName(name);
            doctor.setEmail(email);
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                doctor.setPhoneNumber(phoneNumber);
            }
            if (specialization != null && !specialization.trim().isEmpty()) {
                doctor.setSpecialization(specialization);
            }
            if (yearsOfExperience != null && !yearsOfExperience.trim().isEmpty()) {
                doctor.setYearsOfExperience(Integer.parseInt(yearsOfExperience));
            }
            if (qualification != null && !qualification.trim().isEmpty()) {
                doctor.setQualification(qualification);
            }
            if (licenseNumber != null && !licenseNumber.trim().isEmpty()) {
                doctor.setLicenseNumber(licenseNumber);
            }
            if (about != null && !about.trim().isEmpty()) {
                doctor.setAbout(about);
            }

            // Save the updated doctor
            universalUserService.updateUser(doctor);
            logger.info("Doctor profile updated successfully for: {}", username);

            return "redirect:/doctor/profile?success=true";
        } catch (Exception e) {
            logger.error("Failed to update doctor profile for: {}", username, e);
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctor/edit-profile";
        }
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Authentication authentication,
            Model model) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);

        if (doctor == null) {
            model.addAttribute("error", "Doctor not found");
            return "error";
        }

        try {
            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed");
                    model.addAttribute("doctor", doctor);
                    return "doctor/profile";
                }

                doctorService.updateProfilePicture(doctor.getDoctorId(), file);
                logger.info("Profile picture updated for doctor: {}", username);
            } else {
                model.addAttribute("error", "No file uploaded");
                model.addAttribute("doctor", doctor);
                return "doctor/profile";
            }
        } catch (Exception e) {
            logger.error("Failed to update profile picture for doctor: {}", username, e);
            model.addAttribute("error", "Failed to update profile picture: " + e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctor/profile";
        }

        return "redirect:/doctor/profile";
    }

    @RequestMapping("/settings")
    public String doctorSettings(Model model, Authentication authentication) {
        logger.info("Doctor settings page accessed");
        
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                logger.warn("Non-doctor user attempted to access doctor settings: {}", username);
                return "redirect:/user/dashboard";
            }
            
            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            model.addAttribute("doctor", doctor);
            model.addAttribute("userRole", userRole);
        }
        
        return "doctor/settings";
    }

    // Blog creation endpoints
    @PostMapping("/blog/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createBlogPost(@RequestBody Map<String, Object> blogData,
                                                              Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }

            String username = Helper.getEmailOfLoggedInUser(authentication);
            logger.info("Blog post creation attempt by user: {}", username);
            String userRole = universalUserService.getUserRole(username);

            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can create blog posts");
                return ResponseEntity.badRequest().body(response);
            }

            Doctor doctor = (Doctor) universalUserService.getUserByEmail(username);
            if (doctor == null) {
                response.put("success", false);
                response.put("message", "Doctor not found");
                return ResponseEntity.badRequest().body(response);
            }

            String title = (String) blogData.get("title");
            String content = (String) blogData.get("content");
            String tags = (String) blogData.get("tags");

            logger.info("Blog post data - title: {}, content length: {}, tags: {}", title, content != null ? content.length() : 0, tags);

            if (title == null || title.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Title is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (content == null || content.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Content is required");
                return ResponseEntity.badRequest().body(response);
            }

            AllBlogPost.PostCategory category = AllBlogPost.PostCategory.HEALTH_TIPS;

            AllBlogPost blogPost = AllBlogPost.builder()
                    .title(title)
                    .content(content)
                    .excerpt("")
                    .category(category)
                    .tags(tags != null ? tags : "")
                    .metaDescription("")
                    .metaKeywords("")
                    .authorId(doctor.getDoctorId())
                    .authorType(AllBlogPost.AuthorType.DOCTOR)
                    .authorName(doctor.getName())
                    .authorSpecialization(doctor.getSpecialization())
                    .authorProfilePicture(doctor.getProfilePicture())
                    .status(AllBlogPost.PostStatus.PUBLISHED)
                    .publishedAt(java.time.LocalDateTime.now())
                    .build();

            logger.info("Saving blog post for doctor: {}", doctor.getName());
            AllBlogPost savedPost = allBlogPostService.saveBlogPost(blogPost);
            logger.info("Blog post saved successfully with id: {}", savedPost.getPostId());

            response.put("success", true);
            response.put("message", "Blog post created successfully");
            response.put("postId", savedPost.getPostId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error creating blog post", e);
            response.put("success", false);
            response.put("message", "Error creating blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/blog/submit-review/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitForReview(@PathVariable String postId, 
                                                            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can submit blog posts for review");
                return ResponseEntity.badRequest().body(response);
            }
            
            allBlogPostService.submitForReview(postId);
            
            response.put("success", true);
            response.put("message", "Blog post submitted for review successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error submitting blog post for review", e);
            response.put("success", false);
            response.put("message", "Error submitting blog post for review: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/blog/publish/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> publishPost(@PathVariable String postId, 
                                                          Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can publish blog posts");
                return ResponseEntity.badRequest().body(response);
            }
            
            allBlogPostService.publishPost(postId);
            
            response.put("success", true);
            response.put("message", "Blog post published successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error publishing blog post", e);
            response.put("success", false);
            response.put("message", "Error publishing blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/blog/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBlogPost(@PathVariable String postId, 
                                                          Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can access blog posts");
                return ResponseEntity.badRequest().body(response);
            }
            
            AllBlogPost blogPost = allBlogPostService.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Blog post not found"));
            
            response.put("success", true);
            response.put("postId", blogPost.getPostId());
            response.put("title", blogPost.getTitle());
            response.put("content", blogPost.getContent());
            response.put("excerpt", blogPost.getExcerpt());
            response.put("category", blogPost.getCategory());
            response.put("tags", blogPost.getTags());
            response.put("metaDescription", blogPost.getMetaDescription());
            response.put("metaKeywords", blogPost.getMetaKeywords());
            response.put("status", blogPost.getStatus());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting blog post", e);
            response.put("success", false);
            response.put("message", "Error getting blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/blog/update/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBlogPost(@PathVariable String postId,
                                                             @RequestBody Map<String, Object> blogData,
                                                             Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can update blog posts");
                return ResponseEntity.badRequest().body(response);
            }
            
            String title = (String) blogData.get("title");
            String content = (String) blogData.get("content");
            String excerpt = (String) blogData.get("excerpt");
            String categoryStr = (String) blogData.get("category");
            String tags = (String) blogData.get("tags");
            String metaDescription = (String) blogData.get("metaDescription");
            String metaKeywords = (String) blogData.get("metaKeywords");
            
            if (title == null || title.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Title is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (content == null || content.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Content is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            AllBlogPost.PostCategory category = AllBlogPost.PostCategory.HEALTH_TIPS;
            if (categoryStr != null && !categoryStr.trim().isEmpty()) {
                try {
                    category = AllBlogPost.PostCategory.valueOf(categoryStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid category: {}", categoryStr);
                }
            }
            
            allBlogPostService.updatePost(postId, title, content, excerpt, 
                    category, tags, metaDescription, metaKeywords);
            
            response.put("success", true);
            response.put("message", "Blog post updated successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error updating blog post", e);
            response.put("success", false);
            response.put("message", "Error updating blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/blog/delete/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteBlogPost(@PathVariable String postId,
                                                             Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "User not authenticated");
                return ResponseEntity.badRequest().body(response);
            }
            
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            
            if (!"Doctor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only doctors can delete blog posts");
                return ResponseEntity.badRequest().body(response);
            }
            
            allBlogPostService.deleteBlogPost(postId);
            
            response.put("success", true);
            response.put("message", "Blog post deleted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error deleting blog post", e);
            response.put("success", false);
            response.put("message", "Error deleting blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
