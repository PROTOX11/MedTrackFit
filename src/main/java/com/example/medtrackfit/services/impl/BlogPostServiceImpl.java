package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.BlogPost;
import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.repositories.BlogPostRepository;
import com.example.medtrackfit.services.BlogPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogPostServiceImpl implements BlogPostService {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Override
    public BlogPost saveBlogPost(BlogPost blogPost) {
        logger.info("Saving blog post: {}", blogPost.getTitle());
        return blogPostRepository.save(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlogPost> findById(String postId) {
        logger.debug("Finding blog post by ID: {}", postId);
        return blogPostRepository.findById(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findAll() {
        logger.debug("Finding all blog posts");
        return blogPostRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByDoctor(Doctor doctor) {
        logger.debug("Finding blog posts by doctor: {}", doctor.getEmail());
        return blogPostRepository.findByDoctorOrderByCreatedAtDesc(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByDoctor(Doctor doctor, Pageable pageable) {
        logger.debug("Finding blog posts by doctor with pagination: {}", doctor.getEmail());
        return blogPostRepository.findByDoctorOrderByCreatedAtDesc(doctor, pageable);
    }

    @Override
    public void deleteBlogPost(String postId) {
        logger.info("Deleting blog post: {}", postId);
        blogPostRepository.deleteById(postId);
    }

    @Override
    public void deleteBlogPost(BlogPost blogPost) {
        logger.info("Deleting blog post: {}", blogPost.getPostId());
        blogPostRepository.delete(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByStatus(BlogPost.PostStatus status) {
        logger.debug("Finding blog posts by status: {}", status);
        return blogPostRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByStatus(BlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding blog posts by status with pagination: {}", status);
        return blogPostRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status) {
        logger.debug("Finding blog posts by doctor and status: {}, {}", doctor.getEmail(), status);
        return blogPostRepository.findByDoctorAndStatusOrderByCreatedAtDesc(doctor, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding blog posts by doctor and status with pagination: {}, {}", doctor.getEmail(), status);
        return blogPostRepository.findByDoctorAndStatusOrderByCreatedAtDesc(doctor, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findPublishedPosts() {
        logger.debug("Finding published blog posts");
        return blogPostRepository.findByStatusOrderByPublishedAtDesc(BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findPublishedPosts(Pageable pageable) {
        logger.debug("Finding published blog posts with pagination");
        return blogPostRepository.findByStatusOrderByPublishedAtDesc(BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findPublishedPostsByDoctor(Doctor doctor) {
        logger.debug("Finding published blog posts by doctor: {}", doctor.getEmail());
        return blogPostRepository.findByDoctorAndStatusOrderByCreatedAtDesc(doctor, BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findPublishedPostsByDoctor(Doctor doctor, Pageable pageable) {
        logger.debug("Finding published blog posts by doctor with pagination: {}", doctor.getEmail());
        return blogPostRepository.findByDoctorAndStatusOrderByCreatedAtDesc(doctor, BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByCategory(BlogPost.PostCategory category) {
        logger.debug("Finding blog posts by category: {}", category);
        return blogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByCategory(BlogPost.PostCategory category, Pageable pageable) {
        logger.debug("Finding blog posts by category with pagination: {}", category);
        return blogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByCategoryAndStatus(BlogPost.PostCategory category, BlogPost.PostStatus status) {
        logger.debug("Finding blog posts by category and status: {}, {}", category, status);
        return blogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByCategoryAndStatus(BlogPost.PostCategory category, BlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding blog posts by category and status with pagination: {}, {}", category, status);
        return blogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findFeaturedPosts() {
        logger.debug("Finding featured blog posts");
        return blogPostRepository.findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findFeaturedPosts(Pageable pageable) {
        logger.debug("Finding featured blog posts with pagination");
        return blogPostRepository.findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> searchPosts(String keyword) {
        logger.debug("Searching blog posts with keyword: {}", keyword);
        return blogPostRepository.searchPublishedPosts(BlogPost.PostStatus.PUBLISHED, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> searchPosts(String keyword, Pageable pageable) {
        logger.debug("Searching blog posts with keyword and pagination: {}", keyword);
        return blogPostRepository.searchPublishedPosts(BlogPost.PostStatus.PUBLISHED, keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> searchPostsByDoctor(Doctor doctor, String keyword) {
        logger.debug("Searching blog posts by doctor with keyword: {}, {}", doctor.getEmail(), keyword);
        return blogPostRepository.searchPostsByDoctor(doctor, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> searchPostsByDoctor(Doctor doctor, String keyword, Pageable pageable) {
        logger.debug("Searching blog posts by doctor with keyword and pagination: {}, {}", doctor.getEmail(), keyword);
        return blogPostRepository.searchPostsByDoctor(doctor, keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findMostViewedPosts() {
        logger.debug("Finding most viewed blog posts");
        return blogPostRepository.findByStatusOrderByViewCountDesc(BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findMostViewedPosts(Pageable pageable) {
        logger.debug("Finding most viewed blog posts with pagination");
        return blogPostRepository.findByStatusOrderByViewCountDesc(BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findMostLikedPosts() {
        logger.debug("Finding most liked blog posts");
        return blogPostRepository.findByStatusOrderByLikeCountDesc(BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findMostLikedPosts(Pageable pageable) {
        logger.debug("Finding most liked blog posts with pagination");
        return blogPostRepository.findByStatusOrderByLikeCountDesc(BlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findRecentPosts() {
        logger.debug("Finding recent blog posts");
        return blogPostRepository.findTop10ByStatusOrderByPublishedAtDesc(BlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findRecentPostsByDoctor(Doctor doctor) {
        logger.debug("Finding recent blog posts by doctor: {}", doctor.getEmail());
        return blogPostRepository.findTop5ByDoctorOrderByCreatedAtDesc(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Finding blog posts by date range: {} to {}", startDate, endDate);
        return blogPostRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Finding blog posts by date range with pagination: {} to {}", startDate, endDate);
        return blogPostRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Finding blog posts by doctor and date range: {}, {} to {}", doctor.getEmail(), startDate, endDate);
        return blogPostRepository.findByDoctorAndCreatedAtBetweenOrderByCreatedAtDesc(doctor, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Finding blog posts by doctor and date range with pagination: {}, {} to {}", doctor.getEmail(), startDate, endDate);
        return blogPostRepository.findByDoctorAndCreatedAtBetweenOrderByCreatedAtDesc(doctor, startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPost> findByTag(String tag) {
        logger.debug("Finding blog posts by tag: {}", tag);
        return blogPostRepository.findByTag(BlogPost.PostStatus.PUBLISHED, tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPost> findByTag(String tag, Pageable pageable) {
        logger.debug("Finding blog posts by tag with pagination: {}", tag);
        return blogPostRepository.findByTag(BlogPost.PostStatus.PUBLISHED, tag, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(BlogPost.PostStatus status) {
        logger.debug("Counting blog posts by status: {}", status);
        return blogPostRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDoctorAndStatus(Doctor doctor, BlogPost.PostStatus status) {
        logger.debug("Counting blog posts by doctor and status: {}, {}", doctor.getEmail(), status);
        return blogPostRepository.countByDoctorAndStatus(doctor, status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDoctor(Doctor doctor) {
        logger.debug("Counting blog posts by doctor: {}", doctor.getEmail());
        return blogPostRepository.countByDoctor(doctor);
    }

    @Override
    public BlogPost publishPost(String postId) {
        logger.info("Publishing blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setStatus(BlogPost.PostStatus.PUBLISHED);
            post.setPublishedAt(LocalDateTime.now());
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost unpublishPost(String postId) {
        logger.info("Unpublishing blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setStatus(BlogPost.PostStatus.DRAFT);
            post.setPublishedAt(null);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost archivePost(String postId) {
        logger.info("Archiving blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setStatus(BlogPost.PostStatus.ARCHIVED);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost rejectPost(String postId, String reason) {
        logger.info("Rejecting blog post: {} with reason: {}", postId, reason);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setStatus(BlogPost.PostStatus.REJECTED);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost featurePost(String postId) {
        logger.info("Featuring blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setIsFeatured(true);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost unfeaturePost(String postId) {
        logger.info("Unfeaturing blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setIsFeatured(false);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost incrementViewCount(String postId) {
        logger.debug("Incrementing view count for blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.incrementViewCount();
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost incrementLikeCount(String postId) {
        logger.debug("Incrementing like count for blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.incrementLikeCount();
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost decrementLikeCount(String postId) {
        logger.debug("Decrementing like count for blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.decrementLikeCount();
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost incrementCommentCount(String postId) {
        logger.debug("Incrementing comment count for blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.incrementCommentCount();
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost decrementCommentCount(String postId) {
        logger.debug("Decrementing comment count for blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.decrementCommentCount();
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost createDraft(Doctor doctor, String title, String content) {
        logger.info("Creating draft blog post for doctor: {}", doctor.getEmail());
        BlogPost post = BlogPost.builder()
                .title(title)
                .content(content)
                .doctor(doctor)
                .status(BlogPost.PostStatus.DRAFT)
                .category(BlogPost.PostCategory.HEALTH_TIPS)
                .build();
        return blogPostRepository.save(post);
    }

    @Override
    public BlogPost updatePost(String postId, String title, String content, String excerpt, 
                              BlogPost.PostCategory category, String tags, String metaDescription, 
                              String metaKeywords) {
        logger.info("Updating blog post: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setTitle(title);
            post.setContent(content);
            post.setExcerpt(excerpt);
            post.setCategory(category);
            post.setTags(tags);
            post.setMetaDescription(metaDescription);
            post.setMetaKeywords(metaKeywords);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    public BlogPost submitForReview(String postId) {
        logger.info("Submitting blog post for review: {}", postId);
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            post.setStatus(BlogPost.PostStatus.UNDER_REVIEW);
            return blogPostRepository.save(post);
        }
        throw new RuntimeException("Blog post not found: " + postId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canEditPost(Doctor doctor, String postId) {
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            return post.getDoctor().getDoctorId().equals(doctor.getDoctorId()) && 
                   (post.getStatus() == BlogPost.PostStatus.DRAFT || post.getStatus() == BlogPost.PostStatus.REJECTED);
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDeletePost(Doctor doctor, String postId) {
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            return post.getDoctor().getDoctorId().equals(doctor.getDoctorId());
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canPublishPost(Doctor doctor, String postId) {
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            BlogPost post = postOpt.get();
            return post.getDoctor().getDoctorId().equals(doctor.getDoctorId()) && 
                   post.getStatus() == BlogPost.PostStatus.DRAFT;
        }
        return false;
    }
}
