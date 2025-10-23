package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.entities.Connections;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.services.HealthMentorService;
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
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/mentor")
public class MentorController {

    private Logger logger = LoggerFactory.getLogger(MentorController.class);

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private HealthMentorService healthMentorService;

    @Autowired
    private AllBlogPostService allBlogPostService;

    @Autowired
    private CloudinaryService cloudinaryService;

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
    public String mentorDashboard(Model model, Authentication authentication) {
        logger.info("Mentor dashboard accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor dashboard: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            String mentorId = mentor.getMentorId();

            // Get real statistics from service
            long activePatients = healthMentorService.getActivePatientsCount(mentorId);
            long pendingRequests = healthMentorService.getPendingRequestsCount(mentorId);
            long connectedDoctors = healthMentorService.getConnectedDoctorsCount(mentorId);
            long messagesToday = healthMentorService.getMessagesTodayCount(mentorId);
            List<Connections> connectedDoctorsList = healthMentorService.getConnectedDoctors(mentorId);
            List<SufferingPatient> topPatients = healthMentorService.getTopPatients(mentorId, 5);

            model.addAttribute("mentor", mentor);
            model.addAttribute("userRole", userRole);
            model.addAttribute("activePatients", activePatients);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("connectedDoctors", connectedDoctors);
            model.addAttribute("messagesToday", messagesToday);
            model.addAttribute("connectedDoctorsList", connectedDoctorsList);
            model.addAttribute("topPatients", topPatients);
            model.addAttribute("blogPosts", 5); // Placeholder - would need blog integration
            model.addAttribute("rating", "4.8"); // Placeholder - would need rating system

            logger.info("Mentor dashboard loaded for: {} with {} active patients", mentor.getName(), activePatients);
        }

        return "mentor/dashboard";
    }

    @RequestMapping("/chat")
    public String mentorChat(Model model, Authentication authentication) {
        logger.info("Mentor chat page accessed");

        if (authentication == null) {
            logger.warn("Unauthenticated user attempted to access mentor chat");
            return "redirect:/login";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);

        if (!"HealthMentor".equals(userRole)) {
            logger.warn("Non-mentor user attempted to access mentor chat: {}", username);
            return "redirect:/user/dashboard";
        }

        HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
        model.addAttribute("mentor", mentor);
        model.addAttribute("userRole", userRole);

        // Add sample data for patients and other mentors
        model.addAttribute("patients", createSamplePatients());
        model.addAttribute("mentors", createSampleMentors());

        return "mentor/chat";
    }

    // Sample data methods - in real application, this would come from database
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

        return patients;
    }

    private java.util.List<java.util.Map<String, Object>> createSampleMentors() {
        java.util.List<java.util.Map<String, Object>> mentors = new java.util.ArrayList<>();

        java.util.Map<String, Object> mentor1 = new java.util.HashMap<>();
        mentor1.put("id", "mentor1");
        mentor1.put("name", "Dr. Lisa Thompson");
        mentor1.put("specialty", "Cardiology Mentor");
        mentor1.put("status", "online");
        mentor1.put("lastMessage", "Hi, I have a patient with complex cardiac issues...");
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

        return mentors;
    }

    @RequestMapping("/blog")
    public String mentorBlog(Model model, Authentication authentication,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        logger.info("Mentor blog page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor blog: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            model.addAttribute("mentor", mentor);
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

        return "mentor/blog";
    }

    @RequestMapping("/patients")
    public String mentorPatients(Model model, Authentication authentication) {
        logger.info("Mentor patients page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor patients: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            model.addAttribute("mentor", mentor);
            model.addAttribute("userRole", userRole);
        }

        return "mentor/patients";
    }

    @RequestMapping("/profile")
    public String mentorProfile(Model model, Authentication authentication) {
        logger.info("Mentor profile page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor profile: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            model.addAttribute("mentor", mentor);
            model.addAttribute("userRole", userRole);
        }

        return "mentor/profile";
    }

    @RequestMapping("/edit-profile")
    public String mentorEditProfile(Model model, Authentication authentication) {
        logger.info("Mentor edit profile page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor edit profile: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            model.addAttribute("mentor", mentor);
            model.addAttribute("userRole", userRole);
        }

        return "mentor/edit-profile";
    }

    @PostMapping("/update-profile")
    public String updateMentorProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "areaOfExpertise", required = false) String areaOfExpertise,
            @RequestParam(value = "mentorshipExperience", required = false) String mentorshipExperience,
            @RequestParam(value = "certifications", required = false) String certifications,
            @RequestParam(value = "recoveryStory", required = false) String recoveryStory,
            @RequestParam(value = "about", required = false) String about,
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);

        if (!"HealthMentor".equals(userRole)) {
            logger.warn("Non-mentor user attempted to update mentor profile: {}", username);
            return "redirect:/user/dashboard";
        }

        HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);

        if (mentor == null) {
            model.addAttribute("error", "Mentor not found");
            return "error";
        }

        try {
            // Update basic information
            mentor.setName(name);
            mentor.setEmail(email);
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                mentor.setPhoneNumber(phoneNumber);
            }
            if (areaOfExpertise != null && !areaOfExpertise.trim().isEmpty()) {
                mentor.setAreaOfExpertise(areaOfExpertise);
            }
            if (mentorshipExperience != null && !mentorshipExperience.trim().isEmpty()) {
                mentor.setMentorshipExperience(Integer.parseInt(mentorshipExperience));
            }
            if (certifications != null && !certifications.trim().isEmpty()) {
                mentor.setCertifications(certifications);
            }
            if (recoveryStory != null && !recoveryStory.trim().isEmpty()) {
                mentor.setRecoveryStory(recoveryStory);
            }
            if (about != null && !about.trim().isEmpty()) {
                mentor.setAbout(about);
            }

            // Save the updated mentor
            healthMentorService.saveHealthMentor(mentor);
            logger.info("Mentor profile updated successfully for: {}", username);

            return "redirect:/mentor/profile?success=true";
        } catch (Exception e) {
            logger.error("Failed to update mentor profile for: {}", username, e);
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            model.addAttribute("mentor", mentor);
            return "mentor/edit-profile";
        }
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Authentication authentication,
            Model model) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);

        if (mentor == null) {
            model.addAttribute("error", "Mentor not found");
            return "error";
        }

        try {
            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed");
                    model.addAttribute("mentor", mentor);
                    return "mentor/profile";
                }

                // Note: Need to implement updateProfilePicture method in HealthMentorService
                // For now, we'll skip this functionality
                logger.info("Profile picture update requested for mentor: {}", username);
                model.addAttribute("error", "Profile picture update not yet implemented");
                model.addAttribute("mentor", mentor);
                return "mentor/profile";
            } else {
                model.addAttribute("error", "No file uploaded");
                model.addAttribute("mentor", mentor);
                return "mentor/profile";
            }
        } catch (Exception e) {
            logger.error("Failed to update profile picture for mentor: {}", username, e);
            model.addAttribute("error", "Failed to update profile picture: ");
            model.addAttribute("mentor", mentor);
            return "mentor/profile";
        }
    }

    @RequestMapping("/settings")
    public String mentorSettings(Model model, Authentication authentication) {
        logger.info("Mentor settings page accessed");

        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);

            if (!"HealthMentor".equals(userRole)) {
                logger.warn("Non-mentor user attempted to access mentor settings: {}", username);
                return "redirect:/user/dashboard";
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            model.addAttribute("mentor", mentor);
            model.addAttribute("userRole", userRole);
        }

        return "mentor/settings";
    }

    // Blog creation endpoints
    @PostMapping(value = "/blog/create", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createBlogPost(@RequestParam("title") String title,
                                                              @RequestParam("content") String content,
                                                              @RequestParam(value = "excerpt", required = false) String excerpt,
                                                              @RequestParam(value = "category", required = false) String categoryStr,
                                                              @RequestParam(value = "tags", required = false) String tags,
                                                              @RequestParam(value = "metaDescription", required = false) String metaDescription,
                                                              @RequestParam(value = "metaKeywords", required = false) String metaKeywords,
                                                              @RequestParam(value = "featuredImage", required = false) MultipartFile featuredImage,
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can create blog posts");
                return ResponseEntity.badRequest().body(response);
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            if (mentor == null) {
                response.put("success", false);
                response.put("message", "Mentor not found");
                return ResponseEntity.badRequest().body(response);
            }

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
            if (categoryStr != null && !categoryStr.trim().isEmpty()) {
                try {
                    category = AllBlogPost.PostCategory.valueOf(categoryStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid category: {}", categoryStr);
                }
            }

            AllBlogPost blogPost = AllBlogPost.builder()
                    .title(title)
                    .content(content)
                    .excerpt(excerpt != null ? excerpt : "")
                    .category(category)
                    .tags(tags != null ? tags : "")
                    .metaDescription(metaDescription != null ? metaDescription : "")
                    .metaKeywords(metaKeywords != null ? metaKeywords : "")
                    .authorId(mentor.getMentorId())
                    .authorType(AllBlogPost.AuthorType.MENTOR)
                    .authorName(mentor.getName())
                    .authorSpecialization(mentor.getAreaOfExpertise())
                    .authorProfilePicture(mentor.getProfilePicture())
                    .status(AllBlogPost.PostStatus.PUBLISHED)
                    .publishedAt(java.time.LocalDateTime.now())
                    .build();

            logger.info("Saving blog post for mentor: {}", mentor.getName());
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

    @GetMapping("/blog/posts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMentorPosts(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can access their posts");
                return ResponseEntity.badRequest().body(response);
            }

            HealthMentor mentor = (HealthMentor) universalUserService.getUserByEmail(username);
            Pageable pageable = PageRequest.of(page, size);
            Page<AllBlogPost> posts = allBlogPostService.findByAuthor(mentor.getMentorId(), AllBlogPost.AuthorType.MENTOR, pageable);

            response.put("success", true);
            response.put("posts", posts.getContent());
            response.put("currentPage", posts.getNumber());
            response.put("totalPages", posts.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting mentor posts", e);
            response.put("success", false);
            response.put("message", "Error getting posts: " + e.getMessage());
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can submit blog posts for review");
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can publish blog posts");
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can access blog posts");
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can update blog posts");
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

            if (!"HealthMentor".equals(userRole)) {
                response.put("success", false);
                response.put("message", "Only mentors can delete blog posts");
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

    // Add the old route for backward compatibility
    @RequestMapping("/add")
    public String addMentor() {
        return "user/find_mentor";
    }
}
