package com.example.medtrackfit.config;

import com.example.medtrackfit.controllers.AuthController.SignupRequest;
import com.example.medtrackfit.services.OtpService;
import com.example.medtrackfit.services.RoleBasedUserService;
import com.example.medtrackfit.services.UniversalUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(OtpAuthenticationProvider.class);

    @Autowired
    private OtpService otpService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private RoleBasedUserService roleBasedUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String otp = (String) authentication.getCredentials();

        logger.info("OtpAuthenticationProvider authenticating email: {}", email);

        if (email == null || otp == null) {
            throw new BadCredentialsException("Email and OTP must be provided.");
        }

        // 1. Validate OTP
        boolean isValid = otpService.validateOtp(email, otp);
        if (!isValid) {
            logger.warn("OTP validation failed for email: {}", email);
            throw new BadCredentialsException("Invalid or expired OTP.");
        }

        logger.info("OTP validated successfully for email: {}", email);

        // Get HTTP session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession(true);

        // 2. Check user existence
        UserDetails userDetails = null;
        if (universalUserService.isUserExistByEmail(email)) {
            userDetails = universalUserService.getUserByEmail(email);
            logger.info("User found in database. Loading user details for: {}", email);
        } else {
            // Check for pending signup
            SignupRequest signupReq = (SignupRequest) session.getAttribute("pendingSignup_" + email);
            if (signupReq != null) {
                logger.info("Pending signup found in session. Registering user: {}", email);
                String defaultProfilePicture = "https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=";

                // Normalize role
                String normalizedRole = signupReq.getRole().toLowerCase().replace("_", "");
                if (normalizedRole.equals("user")) {
                    normalizedRole = "sufferingpatient";
                }

                userDetails = roleBasedUserService.saveUserByRole(
                        normalizedRole,
                        signupReq.getName(),
                        signupReq.getEmail(),
                        "NO_PASSWORD", // Password not required for OTP login
                        signupReq.getPhoneNumber(),
                        signupReq.getAbout(),
                        defaultProfilePicture
                );

                // Clear pending signup from session
                session.removeAttribute("pendingSignup_" + email);
                logger.info("User registered successfully. Loaded user details for: {}", email);
            } else {
                logger.warn("No user found and no pending signup for email: {}", email);
                throw new UsernameNotFoundException("User not registered and no pending signup found.");
            }
        }

        // 3. Create fully authenticated token
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );

        // Save context and user details in session
        SecurityContextHolder.getContext().setAuthentication(result);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("loggedInUser", userDetails);

        logger.info("Authentication context established in session for: {}", email);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
