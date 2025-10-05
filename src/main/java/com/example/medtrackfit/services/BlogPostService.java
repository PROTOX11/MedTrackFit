package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.BlogPost;
import com.example.medtrackfit.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BlogPostService {
    
    // Basic CRUD operations
    BlogPost saveBlogPost(BlogPost blogPost);
    
    Optional<BlogPost> findById(String postId);
    
    List<BlogPost> findAll();
    
    List<BlogPost> findByDoctor(Doctor doctor);
    
    Page<BlogPost> findByDoctor(Doctor doctor, Pageable pageable);
    
    void deleteBlogPost(String postId);
    
    void deleteBlogPost(BlogPost blogPost);
    
    // Status-based operations
    List<BlogPost> findByStatus(BlogPost.PostStatus status);
    
    Page<BlogPost> findByStatus(BlogPost.PostStatus status, Pageable pageable);
    
    List<BlogPost> findByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status);
    
    Page<BlogPost> findByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status, Pageable pageable);
    
    // Published posts
    List<BlogPost> findPublishedPosts();
    
    Page<BlogPost> findPublishedPosts(Pageable pageable);
    
    List<BlogPost> findPublishedPostsByDoctor(Doctor doctor);
    
    Page<BlogPost> findPublishedPostsByDoctor(Doctor doctor, Pageable pageable);
    
    // Category-based operations
    List<BlogPost> findByCategory(BlogPost.PostCategory category);
    
    Page<BlogPost> findByCategory(BlogPost.PostCategory category, Pageable pageable);
    
    List<BlogPost> findByCategoryAndStatus(BlogPost.PostCategory category, BlogPost.PostStatus status);
    
    Page<BlogPost> findByCategoryAndStatus(BlogPost.PostCategory category, BlogPost.PostStatus status, Pageable pageable);
    
    // Featured posts
    List<BlogPost> findFeaturedPosts();
    
    Page<BlogPost> findFeaturedPosts(Pageable pageable);
    
    // Search operations
    List<BlogPost> searchPosts(String keyword);
    
    Page<BlogPost> searchPosts(String keyword, Pageable pageable);
    
    List<BlogPost> searchPostsByDoctor(Doctor doctor, String keyword);
    
    Page<BlogPost> searchPostsByDoctor(Doctor doctor, String keyword, Pageable pageable);
    
    // Popular posts
    List<BlogPost> findMostViewedPosts();
    
    Page<BlogPost> findMostViewedPosts(Pageable pageable);
    
    List<BlogPost> findMostLikedPosts();
    
    Page<BlogPost> findMostLikedPosts(Pageable pageable);
    
    // Recent posts
    List<BlogPost> findRecentPosts();
    
    List<BlogPost> findRecentPostsByDoctor(Doctor doctor);
    
    // Date range operations
    List<BlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<BlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<BlogPost> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    
    Page<BlogPost> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Tag operations
    List<BlogPost> findByTag(String tag);
    
    Page<BlogPost> findByTag(String tag, Pageable pageable);
    
    // Statistics
    long countByStatus(BlogPost.PostStatus status);
    
    long countByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status);
    
    long countByDoctor(Doctor doctor);
    
    // Post management operations
    BlogPost publishPost(String postId);
    
    BlogPost unpublishPost(String postId);
    
    BlogPost archivePost(String postId);
    
    BlogPost rejectPost(String postId, String reason);
    
    BlogPost featurePost(String postId);
    
    BlogPost unfeaturePost(String postId);
    
    // View and engagement operations
    BlogPost incrementViewCount(String postId);
    
    BlogPost incrementLikeCount(String postId);
    
    BlogPost decrementLikeCount(String postId);
    
    BlogPost incrementCommentCount(String postId);
    
    BlogPost decrementCommentCount(String postId);
    
    // Utility methods
    BlogPost createDraft(Doctor doctor, String title, String content);
    
    BlogPost updatePost(String postId, String title, String content, String excerpt, 
                       BlogPost.PostCategory category, String tags, String metaDescription, 
                       String metaKeywords);
    
    BlogPost submitForReview(String postId);
    
    boolean canEditPost(Doctor doctor, String postId);
    
    boolean canDeletePost(Doctor doctor, String postId);
    
    boolean canPublishPost(Doctor doctor, String postId);
}
