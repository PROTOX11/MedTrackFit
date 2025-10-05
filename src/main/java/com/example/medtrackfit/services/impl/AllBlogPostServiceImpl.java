package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.repositories.AllBlogPostRepository;
import com.example.medtrackfit.services.AllBlogPostService;
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
public class AllBlogPostServiceImpl implements AllBlogPostService {

    private static final Logger logger = LoggerFactory.getLogger(AllBlogPostServiceImpl.class);

    @Autowired
    private AllBlogPostRepository allBlogPostRepository;

    @Override
    public AllBlogPost saveBlogPost(AllBlogPost blogPost) {
        logger.info("Saving all blog post: {}", blogPost.getTitle());
        return allBlogPostRepository.save(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AllBlogPost> findById(String postId) {
        logger.debug("Finding all blog post by ID: {}", postId);
        return allBlogPostRepository.findById(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findAll() {
        logger.debug("Finding all blog posts");
        return allBlogPostRepository.findAll();
    }

    @Override
    public void deleteBlogPost(String postId) {
        logger.info("Deleting all blog post: {}", postId);
        allBlogPostRepository.deleteById(postId);
    }

    @Override
    public void deleteBlogPost(AllBlogPost blogPost) {
        logger.info("Deleting all blog post: {}", blogPost.getPostId());
        allBlogPostRepository.delete(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByStatus(AllBlogPost.PostStatus status) {
        logger.debug("Finding all blog posts by status: {}", status);
        return allBlogPostRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByStatus(AllBlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding all blog posts by status with pagination: {}", status);
        return allBlogPostRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findPublishedPosts() {
        logger.debug("Finding published all blog posts");
        return allBlogPostRepository.findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findPublishedPosts(Pageable pageable) {
        logger.debug("Finding published all blog posts with pagination");
        return allBlogPostRepository.findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByAuthorType(AllBlogPost.AuthorType authorType) {
        logger.debug("Finding all blog posts by author type: {}", authorType);
        return allBlogPostRepository.findByAuthorTypeAndStatusOrderByCreatedAtDesc(authorType, AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByAuthorType(AllBlogPost.AuthorType authorType, Pageable pageable) {
        logger.debug("Finding all blog posts by author type with pagination: {}", authorType);
        return allBlogPostRepository.findByAuthorTypeAndStatusOrderByCreatedAtDesc(authorType, AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status) {
        logger.debug("Finding all blog posts by author type and status: {}, {}", authorType, status);
        return allBlogPostRepository.findByAuthorTypeAndStatusOrderByCreatedAtDesc(authorType, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding all blog posts by author type and status with pagination: {}, {}", authorType, status);
        return allBlogPostRepository.findByAuthorTypeAndStatusOrderByCreatedAtDesc(authorType, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByCategory(AllBlogPost.PostCategory category) {
        logger.debug("Finding all blog posts by category: {}", category);
        return allBlogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByCategory(AllBlogPost.PostCategory category, Pageable pageable) {
        logger.debug("Finding all blog posts by category with pagination: {}", category);
        return allBlogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByCategoryAndStatus(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status) {
        logger.debug("Finding all blog posts by category and status: {}, {}", category, status);
        return allBlogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByCategoryAndStatus(AllBlogPost.PostCategory category, AllBlogPost.PostStatus status, Pageable pageable) {
        logger.debug("Finding all blog posts by category and status with pagination: {}, {}", category, status);
        return allBlogPostRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findFeaturedPosts() {
        logger.debug("Finding featured all blog posts");
        return allBlogPostRepository.findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findFeaturedPosts(Pageable pageable) {
        logger.debug("Finding featured all blog posts with pagination");
        return allBlogPostRepository.findByIsFeaturedTrueAndStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> searchPosts(String keyword) {
        logger.debug("Searching all blog posts with keyword: {}", keyword);
        return allBlogPostRepository.searchPublishedPosts(AllBlogPost.PostStatus.PUBLISHED, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> searchPosts(String keyword, Pageable pageable) {
        logger.debug("Searching all blog posts with keyword and pagination: {}", keyword);
        return allBlogPostRepository.searchPublishedPosts(AllBlogPost.PostStatus.PUBLISHED, keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> searchPostsByAuthorType(AllBlogPost.AuthorType authorType, String keyword) {
        logger.debug("Searching all blog posts by author type with keyword: {}, {}", authorType, keyword);
        return allBlogPostRepository.searchPostsByAuthorType(authorType, AllBlogPost.PostStatus.PUBLISHED, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findMostViewedPosts() {
        logger.debug("Finding most viewed all blog posts");
        return allBlogPostRepository.findByStatusOrderByViewCountDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findMostViewedPosts(Pageable pageable) {
        logger.debug("Finding most viewed all blog posts with pagination");
        return allBlogPostRepository.findByStatusOrderByViewCountDesc(AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findMostLikedPosts() {
        logger.debug("Finding most liked all blog posts");
        return allBlogPostRepository.findByStatusOrderByLikeCountDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findMostLikedPosts(Pageable pageable) {
        logger.debug("Finding most liked all blog posts with pagination");
        return allBlogPostRepository.findByStatusOrderByLikeCountDesc(AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findRecentPosts() {
        logger.debug("Finding recent all blog posts");
        return allBlogPostRepository.findTop10ByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Finding all blog posts by date range: {} to {}", startDate, endDate);
        return allBlogPostRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Finding all blog posts by date range with pagination: {} to {}", startDate, endDate);
        return allBlogPostRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByTag(String tag) {
        logger.debug("Finding all blog posts by tag: {}", tag);
        return allBlogPostRepository.findByTag(AllBlogPost.PostStatus.PUBLISHED, tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByTag(String tag, Pageable pageable) {
        logger.debug("Finding all blog posts by tag with pagination: {}", tag);
        return allBlogPostRepository.findByTag(AllBlogPost.PostStatus.PUBLISHED, tag, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AllBlogPost.PostStatus status) {
        logger.debug("Counting all blog posts by status: {}", status);
        return allBlogPostRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAuthorTypeAndStatus(AllBlogPost.AuthorType authorType, AllBlogPost.PostStatus status) {
        logger.debug("Counting all blog posts by author type and status: {}, {}", authorType, status);
        return allBlogPostRepository.countByAuthorTypeAndStatus(authorType, status);
    }

    @Override
    public AllBlogPost publishPost(String postId) {
        logger.info("Publishing all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setStatus(AllBlogPost.PostStatus.PUBLISHED);
            post.setPublishedAt(LocalDateTime.now());
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost unpublishPost(String postId) {
        logger.info("Unpublishing all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setStatus(AllBlogPost.PostStatus.DRAFT);
            post.setPublishedAt(null);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost archivePost(String postId) {
        logger.info("Archiving all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setStatus(AllBlogPost.PostStatus.ARCHIVED);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost rejectPost(String postId, String reason) {
        logger.info("Rejecting all blog post: {} with reason: {}", postId, reason);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setStatus(AllBlogPost.PostStatus.REJECTED);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost featurePost(String postId) {
        logger.info("Featuring all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setIsFeatured(true);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost unfeaturePost(String postId) {
        logger.info("Unfeaturing all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setIsFeatured(false);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost incrementViewCount(String postId) {
        logger.debug("Incrementing view count for all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.incrementViewCount();
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost incrementLikeCount(String postId) {
        logger.debug("Incrementing like count for all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.incrementLikeCount();
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost decrementLikeCount(String postId) {
        logger.debug("Decrementing like count for all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.decrementLikeCount();
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost incrementCommentCount(String postId) {
        logger.debug("Incrementing comment count for all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.incrementCommentCount();
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost decrementCommentCount(String postId) {
        logger.debug("Decrementing comment count for all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.decrementCommentCount();
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost createDraft(String authorId, AllBlogPost.AuthorType authorType, String authorName, 
                                  String title, String content, String authorSpecialization, String authorProfilePicture) {
        logger.info("Creating draft all blog post for author: {}", authorId);
        AllBlogPost post = AllBlogPost.builder()
                .title(title)
                .content(content)
                .authorId(authorId)
                .authorType(authorType)
                .authorName(authorName)
                .authorSpecialization(authorSpecialization)
                .authorProfilePicture(authorProfilePicture)
                .status(AllBlogPost.PostStatus.DRAFT)
                .category(AllBlogPost.PostCategory.HEALTH_TIPS)
                .build();
        return allBlogPostRepository.save(post);
    }

    @Override
    public AllBlogPost updatePost(String postId, String title, String content, String excerpt, 
                                 AllBlogPost.PostCategory category, String tags, String metaDescription, 
                                 String metaKeywords) {
        logger.info("Updating all blog post: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setTitle(title);
            post.setContent(content);
            post.setExcerpt(excerpt);
            post.setCategory(category);
            post.setTags(tags);
            post.setMetaDescription(metaDescription);
            post.setMetaKeywords(metaKeywords);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    public AllBlogPost submitForReview(String postId) {
        logger.info("Submitting all blog post for review: {}", postId);
        Optional<AllBlogPost> postOpt = allBlogPostRepository.findById(postId);
        if (postOpt.isPresent()) {
            AllBlogPost post = postOpt.get();
            post.setStatus(AllBlogPost.PostStatus.UNDER_REVIEW);
            return allBlogPostRepository.save(post);
        }
        throw new RuntimeException("All blog post not found: " + postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findAllPublicBlogs() {
        logger.debug("Finding all public blog posts from all user types");
        return allBlogPostRepository.findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findAllPublicBlogs(Pageable pageable) {
        logger.debug("Finding all public blog posts from all user types with pagination");
        return allBlogPostRepository.findByStatusOrderByPublishedAtDesc(AllBlogPost.PostStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllBlogPost> findByAuthor(String authorId, AllBlogPost.AuthorType authorType) {
        logger.debug("Finding all blog posts by author: {}, {}", authorId, authorType);
        return allBlogPostRepository.findByAuthorIdAndAuthorTypeOrderByCreatedAtDesc(authorId, authorType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllBlogPost> findByAuthor(String authorId, AllBlogPost.AuthorType authorType, Pageable pageable) {
        logger.debug("Finding all blog posts by author with pagination: {}, {}", authorId, authorType);
        return allBlogPostRepository.findByAuthorIdAndAuthorTypeOrderByCreatedAtDesc(authorId, authorType, pageable);
    }
}
