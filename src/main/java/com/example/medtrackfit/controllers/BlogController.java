package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.RecoveredPatient;
import com.example.medtrackfit.services.AllBlogPostService;
import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private AllBlogPostService allBlogPostService;

    @Autowired
    private UniversalUserService universalUserService;

    @GetMapping("/public")
    public String publicBlogs(Model model, 
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        logger.info("Public blogs page accessed");
        
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
        
        return "blog/public";
    }

    @PostMapping("/create")
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
            
            // Get user object based on role
            Object user = universalUserService.getUserByEmail(username);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
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
            
            // Determine author type and extract user information
            AllBlogPost.AuthorType authorType;
            String authorId;
            String authorName;
            String authorSpecialization = null;
            String authorProfilePicture = null;
            
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                authorType = AllBlogPost.AuthorType.DOCTOR;
                authorId = doctor.getDoctorId();
                authorName = doctor.getName();
                authorSpecialization = doctor.getSpecialization();
                authorProfilePicture = doctor.getProfilePicture();
            } else if (user instanceof HealthMentor) {
                HealthMentor mentor = (HealthMentor) user;
                authorType = AllBlogPost.AuthorType.MENTOR;
                authorId = mentor.getMentorId();
                authorName = mentor.getName();
                authorSpecialization = mentor.getAreaOfExpertise();
                authorProfilePicture = mentor.getProfilePicture();
            } else if (user instanceof RecoveredPatient) {
                RecoveredPatient patient = (RecoveredPatient) user;
                authorType = AllBlogPost.AuthorType.RECOVERED_PATIENT;
                authorId = patient.getPatientId();
                authorName = patient.getName();
                authorSpecialization = patient.getPreviousCondition();
                authorProfilePicture = patient.getProfilePicture();
            } else {
                response.put("success", false);
                response.put("message", "Invalid user type for blog creation");
                return ResponseEntity.badRequest().body(response);
            }
            
            AllBlogPost blogPost = AllBlogPost.builder()
                    .title(title)
                    .content(content)
                    .excerpt(excerpt != null ? excerpt : "")
                    .category(category)
                    .tags(tags != null ? tags : "")
                    .metaDescription(metaDescription != null ? metaDescription : "")
                    .metaKeywords(metaKeywords != null ? metaKeywords : "")
                    .authorId(authorId)
                    .authorType(authorType)
                    .authorName(authorName)
                    .authorSpecialization(authorSpecialization)
                    .authorProfilePicture(authorProfilePicture)
                    .status(AllBlogPost.PostStatus.DRAFT)
                    .build();
            
            AllBlogPost savedPost = allBlogPostService.saveBlogPost(blogPost);
            
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

    @PostMapping("/submit-review/{postId}")
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

    @PostMapping("/publish/{postId}")
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

    @GetMapping("/{postId}")
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
            response.put("authorName", blogPost.getAuthorName());
            response.put("authorType", blogPost.getAuthorType());
            response.put("authorSpecialization", blogPost.getAuthorSpecialization());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting blog post", e);
            response.put("success", false);
            response.put("message", "Error getting blog post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/update/{postId}")
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

    @PostMapping("/delete/{postId}")
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
