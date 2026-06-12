package com.example.medtrackfit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${app.mail.from:medtrackfit.help@gmail.com}")
    private String mailFrom;

    public void sendOtpEmail(String to, String otp) {
        logger.info("Initiating email send request. From: {}, To: {}", mailFrom, to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(to);
            message.setSubject("Your MedTrackFit Verification Code");
            message.setText("Your verification code is: " + otp + "\n\nThis code is valid for 5 minutes. Do not share it with anyone.");

            logger.debug("SMTP Host Connection details and message structure generated. Sending mail now...");
            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}. Error type: {}, Message: {}", 
                         to, e.getClass().getName(), e.getMessage(), e);
            throw new RuntimeException("Email sending failed due to SMTP/Connection issue: " + e.getMessage(), e);
        }
    }

    public void sendContactEmail(String fromName, String fromEmail, String subject, String messageText) {
        logger.info("Sending contact form email from {} <{}>", fromName, fromEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo("medtrackfit.help@gmail.com");
            message.setSubject("MedTrackFit Contact Request: " + subject);
            message.setText("New support request received:\n\n" +
                    "Name: " + fromName + "\n" +
                    "Email: " + fromEmail + "\n" +
                    "Subject: " + subject + "\n\n" +
                    "Message:\n" + messageText);
            javaMailSender.send(message);
            logger.info("Contact email sent successfully to: {}", "medtrackfit.help@gmail.com");
        } catch (Exception e) {
            logger.error("Failed to send contact email: {}", e.getMessage(), e);
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
}
