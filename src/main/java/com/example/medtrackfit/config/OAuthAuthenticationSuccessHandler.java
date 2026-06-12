package com.example.medtrackfit.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.example.medtrackfit.entities.Providers;
import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.services.UserService;
import com.medtrackfit.helper.AppConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private com.example.medtrackfit.services.UniversalUserService universalUserService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        logger.info("OAuthAuthenticationSuccessHandler");

        // Identify the provider
        String authorizedClientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        logger.info("Authorized Client Registration ID: {}", authorizedClientRegistrationId);

        // Replace 'var' with 'DefaultOAuth2User'
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info("{} => {}", key, value);
        });

        // Prepare the User entity
        User user = User.builder()
                .roleList(List.of(AppConstants.ROLE_USER))
                .emailVerified(true)
                .enabled(true)
                .build();

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePicture(null);
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setPassword("dummy");
            user.setAbout("Hey there! I am new here, signed up via Google");
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            String email = oauthUser.getAttribute("email") != null
                    ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@github.com";
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePicture(null);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
            user.setPassword("dummy");
            user.setAbout("Account created using GitHub");
        } else {
            logger.info("Unknown provider: {}", authorizedClientRegistrationId);
            response.sendRedirect("/error");
            return;
        }

        String role = universalUserService.getUserRole(user.getEmail());
        if (role != null) {
            logger.info("Existing role-based user logging in via Google: {}, role: {}", user.getEmail(), role);
            String prefix;
            switch (role.toLowerCase()) {
                case "doctor":
                    prefix = "/doctor";
                    break;
                case "healthmentor":
                case "mentor":
                    prefix = "/mentor";
                    break;
                case "recoveredpatient":
                    prefix = "/recoveredpatient";
                    break;
                case "sufferingpatient":
                default:
                    prefix = "/suff-pat";
                    break;
            }
            response.sendRedirect(prefix + "/dashboard");
        } else {
            logger.info("New OAuth user signing in: {}", user.getEmail());
            // Save to generic User table so they can authenticate
            if (!userService.isUserExistByEmail(user.getEmail())) {
                userService.saveUser(user);
                logger.info("Generic user saved for OAuth: {}", user.getEmail());
            }
            // Redirect to dashboard where popup will be shown
            response.sendRedirect("/suff-pat/dashboard");
        }
    }
}