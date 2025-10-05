package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.AllBlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AllBlogPostRepository extends JpaRepository<AllBlogPost, String> {

    // Find posts by status
    List<AllBlogPost> findByStatusOrderByCreatedAtDesc(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByStatusOrderByCreatedAtDesc(AllBlogPost.PostStatus status, Pageable pageable);

    // Find published posts
    List<AllBlogPost> findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus status, Pageable pageable);

    // Find posts by author type
    List<AllBlogPost> findByAuthorTypeAndStatusOrderByCreatedAtDesc(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByAuthorTypeAndStatusOrderByCreatedAtDesc(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status, Pageable pageable);

    // Find posts by category
    List<AllBlogPost> findByCategoryAndStatusOrderByCreatedAtDesc(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByCategoryAndStatusOrderByCreatedAtDesc(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status, Pageable pageable);

    // Find featured posts
    List<AllBlogPost> findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus status, Pageable pageable);

    // Search posts by title or content
    @Query("SELECT abp FROM AllBlogPost abp WHERE abp.status = :status AND " +
           "(LOWER(abp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY abp.publishedAt DESC")
    List<AllBlogPost> searchPublishedPosts(@Param("status") AllBlogPost.PostStatus status, @Param("keyword") String keyword);
    
    @Query("SELECT abp FROM AllBlogPost abp WHERE abp.status = :status AND " +
           "(LOWER(abp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY abp.publishedAt DESC")
    Page<AllBlogPost> searchPublishedPosts(@Param("status") AllBlogPost.PostStatus status, @Param("keyword") String keyword, Pageable pageable);

    // Find most viewed posts
    List<AllBlogPost> findByStatusOrderByViewCountDesc(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByStatusOrderByViewCountDesc(AllBlogPost.PostStatus status, Pageable pageable);

    // Find most liked posts
    List<AllBlogPost> findByStatusOrderByLikeCountDesc(AllBlogPost.PostStatus status);
    
    Page<AllBlogPost> findByStatusOrderByLikeCountDesc(AllBlogPost.PostStatus status, Pageable pageable);

    // Find posts created in date range
    List<AllBlogPost> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<AllBlogPost> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find posts by author
    List<AllBlogPost> findByAuthorIdAndAuthorTypeOrderByCreatedAtDesc(String authorId, AllBlogPost.AuthorType authorType);
    
    Page<AllBlogPost> findByAuthorIdAndAuthorTypeOrderByCreatedAtDesc(String authorId, AllBlogPost.AuthorType authorType, Pageable pageable);

    // Count posts by status
    long countByStatus(AllBlogPost.PostStatus status);
    
    long countByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status);

    // Find posts by tags
    @Query("SELECT abp FROM AllBlogPost abp WHERE abp.status = :status AND " +
           "LOWER(abp.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "ORDER BY abp.publishedAt DESC")
    List<AllBlogPost> findByTag(@Param("status") AllBlogPost.PostStatus status, @Param("tag") String tag);
    
    @Query("SELECT abp FROM AllBlogPost abp WHERE abp.status = :status AND " +
           "LOWER(abp.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "ORDER BY abp.publishedAt DESC")
    Page<AllBlogPost> findByTag(@Param("status") AllBlogPost.PostStatus status, @Param("tag") String tag, Pageable pageable);

    // Find recent posts
    List<AllBlogPost> findTop10ByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus status);
    
    List<AllBlogPost> findTop5ByAuthorIdAndAuthorTypeOrderByCreatedAtDesc(String authorId, AllBlogPost.AuthorType authorType);

    // Find posts by author type with search
    @Query("SELECT abp FROM AllBlogPost abp WHERE abp.authorType = :authorType AND abp.status = :status AND " +
           "(LOWER(abp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(abp.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY abp.publishedAt DESC")
    List<AllBlogPost> searchPostsByAuthorType(@Param("authorType") AllBlogPost.AuthorType authorType, 
                                               @Param("status") AllBlogPost.PostStatus status, 
                                               @Param("keyword") String keyword);
}
