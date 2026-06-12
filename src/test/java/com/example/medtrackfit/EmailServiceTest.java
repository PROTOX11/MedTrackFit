package com.example.medtrackfit;

import com.example.medtrackfit.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        emailService.sendOtpEmail("prakashstorage002@gmail.com", "999999");
        System.out.println("Email test completed.");
    }
}
