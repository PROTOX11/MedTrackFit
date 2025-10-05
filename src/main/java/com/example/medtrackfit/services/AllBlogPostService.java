package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.AllBlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AllBlogPostService {
    
    // Basic CRUD operations
    AllBlogPost saveBlogPost(AllBlogPost blogPost);
    
    Optional<AllBlogPost> findById(String postId);
    
    List<AllBlogPost> findAll();
    
    void deleteBlogPost(String postId);
    
    void deleteBlogPost(AllBlogPost blogPost);
    
    // Status-based operations
    List<AllBlogPost> findByStatus(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByStatus(AllBlogPost.PostStatus status, Pageable pageable);
    
    // Published posts
    List<AllBlogPost> findPublishedPosts();
    
    Page<AllBlogPost> findPublishedPosts(Pageable pageable);
    
    // Author type operations
    List<AllBlogPost> findByAuthorType(AllBlogPost.AuthorType authorType);
    
    Page<AllBlogPost> findByAuthorType(AllBlogPost.AuthorType authorType, Pageable pageable);
    
    List<AllBlogPost> findByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status, Pageable pageable);
    
    // Category-based operations
    List<AllBlogPost> findByCategory(AllBlogPost.PostCategory category);
    
    Page<AllBlogPost> findByCategory(AllBlogPost.PostCategory category, Pageable pageable);
    
    List<AllBlogPost> findByCategoryAndStatus(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByCategoryAndStatus(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status, Pageable pageable);
    
    // Featured posts
    List<AllBlogPost> findFeaturedPosts();
    
    Page<AllBlogPost> findFeaturedPosts(Pageable pageable);
    
    // Search operations
    List<AllBlogPost> searchPosts(String keyword);
    
    Page<AllBlogPost> searchPosts(String keyword, Pageable pageable);
    
    List<AllBlogPost> searchPostsByAuthorType(AllBlogPost.AuthorType authorType, String keyword);
    
    // Popular posts
    List<AllBlogPost> findMostViewedPosts();
    
    Page<AllBlogPost> findMostViewedPosts(Pageable pageable);
    
    List<AllBlogPost> findMostLikedPosts();
    
    Page<AllBlogPost> findMostLikedPosts(Pageable pageable);
    
    // Recent posts
    List<AllBlogPost> findRecentPosts();
    
    // Date range operations
    List<AllBlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<AllBlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Tag operations
    List<AllBlogPost> findByTag(String tag);
    
    Page<AllBlogPost> findByTag(String tag, Pageable pageable);
    
    // Statistics
    long countByStatus(AllBlogPost.PostStatus status);
    
    long countByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status);
    
    // Post management operations
    AllBlogPost publishPost(String postId);
    
    AllBlogPost unpublishPost(String postId);
    
    AllBlogPost archivePost(String postId);
    
    AllBlogPost rejectPost(String postId, String reason);
    
    AllBlogPost featurePost(String postId);
    
    AllBlogPost unfeaturePost(String postId);
    
    // View and engagement operations
    AllBlogPost incrementViewCount(String postId);
    
    AllBlogPost incrementLikeCount(String postId);
    
    AllBlogPost decrementLikeCount(String postId);
    
    AllBlogPost incrementCommentCount(String postId);
    
    AllBlogPost decrementCommentCount(String postId);
    
    // Utility methods
    AllBlogPost createDraft(String authorId, AllBlogPost.AuthorType authorType, String authorName, 
                           String title, String content, String authorSpecialization, String authorProfilePicture);
    
    AllBlogPost updatePost(String postId, String title, String content, String excerpt, 
                          AllBlogPost.PostCategory category, String tags, String metaDescription, 
                          String metaKeywords);
    
    AllBlogPost submitForReview(String postId);
    
    // Get all public blogs from all user types
    List<AllBlogPost> findAllPublicBlogs();
    
    Page<AllBlogPost> findAllPublicBlogs(Pageable pageable);
    
    // Get blogs by specific author
    List<AllBlogPost> findByAuthor(String authorId, AllBlogPost.AuthorType authorType);
    
    Page<AllBlogPost> findByAuthor(String authorId, AllBlogPost.AuthorType authorType, Pageable pageable);
}
