package com.example.medtrackfit.config;

import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private UniversalUserService universalUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        System.out.println("Custom authentication success handler called");
        System.out.println("Authentication: " + authentication);
        System.out.println("Is authenticated: " + authentication.isAuthenticated());
        
        // Get user role and redirect accordingly
        String username = Helper.getEmailOfLoggedInUser(authentication);
        String userRole = universalUserService.getUserRole(username);
        
        if ("Doctor".equals(userRole)) {
            // Redirect doctors to dashboard page
            response.sendRedirect("/doctor/dashboard");
        } else {
            // Redirect other users to dashboard
            response.sendRedirect("/user/dashboard");
        }
    }
}
