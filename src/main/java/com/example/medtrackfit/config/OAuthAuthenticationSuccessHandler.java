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
                .userId(UUID.randomUUID().toString())
                .roleList(List.of(AppConstants.ROLE_USER))
                .emailVerified(true)
                .enabled(true)
                .build();

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePicture(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setPassword("dummy");
            user.setAbout("Hey there! I am new here, signed up via Google");
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            String email = oauthUser.getAttribute("email") != null
                    ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@github.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePicture(picture);
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

        // Check if the user already exists, if not, save them using UserService
        if (!userService.isUserExistByEmail(user.getEmail())) {
            userService.saveUser(user);
            logger.info("User saved: {}", user.getEmail());
        } else {
            logger.info("User already exists: {}", user.getEmail());
        }

        // Redirect to the dashboard
        response.sendRedirect("/user/dashboard");
    }
}