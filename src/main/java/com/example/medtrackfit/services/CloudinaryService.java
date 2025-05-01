package com.example.medtrackfit.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            logger.error("File is null or empty");
            throw new IllegalArgumentException("File is required");
        }
        if (!file.getContentType().startsWith("image/")) {
            logger.error("Invalid file type: {}", file.getContentType());
            throw new IllegalArgumentException("File must be an image");
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "user_profile_picture"));
            String imageUrl = (String) uploadResult.get("url");
            logger.info("Image uploaded to Cloudinary successfully. URL: {}", imageUrl);
            return imageUrl;
        } catch (Exception e) {
            logger.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }
}