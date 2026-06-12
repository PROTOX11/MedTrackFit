package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.OtpEntity;
import com.example.medtrackfit.repositories.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private OtpRepository otpRepository;

    /**
     * Generates a 6-digit OTP, stores it in the database with a 5-minute expiry,
     * and returns the generated OTP.
     */
    @Transactional
    public String generateAndStoreOtp(String email) {
        logger.info("Generating OTP for email: {}", email);
        String otp = generateRandomOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        try {
            // Check if there is an existing OTP for this email
            Optional<OtpEntity> existingOtpOpt = otpRepository.findByEmail(email);
            OtpEntity otpEntity;
            if (existingOtpOpt.isPresent()) {
                logger.debug("Existing OTP found for {}. Updating it with new code and expiry.", email);
                otpEntity = existingOtpOpt.get();
                otpEntity.setOtp(otp);
                otpEntity.setExpiryTime(expiryTime);
            } else {
                logger.debug("No existing OTP found for {}. Creating a new record.", email);
                otpEntity = OtpEntity.builder()
                        .email(email)
                        .otp(otp)
                        .expiryTime(expiryTime)
                        .build();
            }

            otpRepository.save(otpEntity);
            logger.info("OTP successfully stored in database for email: {}", email);
            return otp;
        } catch (Exception e) {
            logger.error("Database error while storing OTP for {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to process and store OTP in database", e);
        }
    }

    /**
     * Validates the provided OTP. Deletes the OTP record from database after successful validation.
     */
    @Transactional
    public boolean validateOtp(String email, String inputOtp) {
        logger.info("Validating OTP for email: {}", email);
        try {
            Optional<OtpEntity> otpOpt = otpRepository.findByEmail(email);
            if (otpOpt.isEmpty()) {
                logger.warn("Validation failed: No OTP record found for email: {}", email);
                return false;
            }

            OtpEntity otpEntity = otpOpt.get();

            // Check if expired
            if (LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {
                logger.warn("Validation failed: OTP for {} has expired. Expiry: {}, Current: {}", 
                            email, otpEntity.getExpiryTime(), LocalDateTime.now());
                otpRepository.delete(otpEntity);
                logger.debug("Expired OTP record deleted for {}", email);
                return false;
            }

            // Validate code
            if (otpEntity.getOtp().equals(inputOtp)) {
                otpRepository.delete(otpEntity);
                logger.info("Validation successful: OTP matches for email: {}. Deleted OTP record.", email);
                return true;
            }

            logger.warn("Validation failed: Incorrect OTP entered for email: {}", email);
            return false;
        } catch (Exception e) {
            logger.error("Database error while validating OTP for {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to validate OTP in database", e);
        }
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit number
        String code = String.valueOf(otp);
        logger.debug("Generated 6-digit OTP: {}", code);
        return code;
    }
}
