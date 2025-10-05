package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.BlogPost;
import com.example.medtrackfit.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {

    // Find posts by doctor
    List<BlogPost> findByDoctorOrderByCreatedAtDesc(Doctor doctor);
    
    Page<BlogPost> findByDoctorOrderByCreatedAtDesc(Doctor doctor, Pageable pageable);

    // Find posts by status
    List<BlogPost> findByStatusOrderByCreatedAtDesc(BlogPost.PostStatus status);
    
    Page<BlogPost> findByStatusOrderByCreatedAtDesc(BlogPost.PostStatus status, Pageable pageable);

    // Find posts by doctor and status
    List<BlogPost> findByDoctorAndStatusOrderByCreatedAtDesc(Doctor doctor, BlogPost.PostStatus status);
    
    Page<BlogPost> findByDoctorAndStatusOrderByCreatedAtDesc(Doctor doctor, BlogPost.PostStatus status, Pageable pageable);

    // Find published posts
    List<BlogPost> findByStatusOrderByPublishedAtDesc(BlogPost.PostStatus status);
    
    Page<BlogPost> findByStatusOrderByPublishedAtDesc(BlogPost.PostStatus status, Pageable pageable);

    // Find posts by category
    List<BlogPost> findByCategoryAndStatusOrderByCreatedAtDesc(BlogPost.PostCategory category, BlogPost.PostStatus status);
    
    Page<BlogPost> findByCategoryAndStatusOrderByCreatedAtDesc(BlogPost.PostCategory category, BlogPost.PostStatus status, Pageable pageable);

    // Find featured posts
    List<BlogPost> findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(BlogPost.PostStatus status);
    
    Page<BlogPost> findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(BlogPost.PostStatus status, Pageable pageable);

    // Search posts by title or content
    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND " +
           "(LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY bp.publishedAt DESC")
    List<BlogPost> searchPublishedPosts(@Param("status") BlogPost.PostStatus status, @Param("keyword") String keyword);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND " +
           "(LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY bp.publishedAt DESC")
    Page<BlogPost> searchPublishedPosts(@Param("status") BlogPost.PostStatus status, @Param("keyword") String keyword, Pageable pageable);

    // Find posts by doctor with search
    @Query("SELECT bp FROM BlogPost bp WHERE bp.doctor = :doctor AND " +
           "(LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY bp.createdAt DESC")
    List<BlogPost> searchPostsByDoctor(@Param("doctor") Doctor doctor, @Param("keyword") String keyword);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.doctor = :doctor AND " +
           "(LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY bp.createdAt DESC")
    Page<BlogPost> searchPostsByDoctor(@Param("doctor") Doctor doctor, @Param("keyword") String keyword, Pageable pageable);

    // Find most viewed posts
    List<BlogPost> findByStatusOrderByViewCountDesc(BlogPost.PostStatus status);
    
    Page<BlogPost> findByStatusOrderByViewCountDesc(BlogPost.PostStatus status, Pageable pageable);

    // Find most liked posts
    List<BlogPost> findByStatusOrderByLikeCountDesc(BlogPost.PostStatus status);
    
    Page<BlogPost> findByStatusOrderByLikeCountDesc(BlogPost.PostStatus status, Pageable pageable);

    // Find posts created in date range
    List<BlogPost> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<BlogPost> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find posts by doctor in date range
    List<BlogPost> findByDoctorAndCreatedAtBetweenOrderByCreatedAtDesc(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    
    Page<BlogPost> findByDoctorAndCreatedAtBetweenOrderByCreatedAtDesc(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Count posts by status
    long countByStatus(BlogPost.PostStatus status);
    
    long countByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status);
    
    long countByDoctor(Doctor doctor);

    // Find posts by tags
    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "ORDER BY bp.publishedAt DESC")
    List<BlogPost> findByTag(@Param("status") BlogPost.PostStatus status, @Param("tag") String tag);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND " +
           "LOWER(bp.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "ORDER BY bp.publishedAt DESC")
    Page<BlogPost> findByTag(@Param("status") BlogPost.PostStatus status, @Param("tag") String tag, Pageable pageable);

    // Find recent posts
    List<BlogPost> findTop10ByStatusOrderByPublishedAtDesc(BlogPost.PostStatus status);
    
    List<BlogPost> findTop5ByDoctorOrderByCreatedAtDesc(Doctor doctor);
}
