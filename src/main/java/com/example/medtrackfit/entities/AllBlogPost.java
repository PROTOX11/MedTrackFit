package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "all_blog_posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllBlogPost {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String postId;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "excerpt", length = 1000)
    private String excerpt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PostStatus status = PostStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PostCategory category;

    @Column(name = "tags")
    private String tags; // Comma-separated tags

    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @Column(name = "comment_count")
    @Builder.Default
    private Long commentCount = 0L;

    @Column(name = "reading_time")
    private Integer readingTime; // in minutes

    // Author information - can be doctor, mentor, or recovered patient
    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "author_type", nullable = false)
    private AuthorType authorType;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "author_specialization")
    private String authorSpecialization;

    @Column(name = "author_profile_picture")
    private String authorProfilePicture;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    // Helper methods
    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public boolean isPublished() {
        return status == PostStatus.PUBLISHED;
    }

    public boolean isDraft() {
        return status == PostStatus.DRAFT;
    }

    public boolean isUnderReview() {
        return status == PostStatus.UNDER_REVIEW;
    }

    public enum PostStatus {
        DRAFT,
        UNDER_REVIEW,
        PUBLISHED,
        ARCHIVED,
        REJECTED
    }

    public enum PostCategory {
        HEALTH_TIPS("Health Tips"),
        MEDICAL_NEWS("Medical News"),
        TREATMENT_GUIDES("Treatment Guides"),
        RESEARCH("Research"),
        CASE_STUDIES("Case Studies"),
        PREVENTIVE_CARE("Preventive Care"),
        MENTAL_HEALTH("Mental Health"),
        NUTRITION("Nutrition"),
        FITNESS("Fitness"),
        CHRONIC_DISEASE("Chronic Disease Management"),
        RECOVERY_STORY("Recovery Story"),
        PATIENT_EXPERIENCE("Patient Experience"),
        MENTOR_ADVICE("Mentor Advice");

        private final String displayName;

        PostCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AuthorType {
        DOCTOR,
        MENTOR,
        RECOVERED_PATIENT
    }
}
