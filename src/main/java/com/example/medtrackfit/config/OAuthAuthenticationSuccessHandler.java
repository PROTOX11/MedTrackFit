package com.example.medtrackfit.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.example.medtrackfit.entities.Providers;
import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.repo.UserRepo;
import com.medtrackfit.helper.AppConstants;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        logger.info("OAuthAuthenticationSuccessHandler");
        





        // identify the provider

        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        DefaultOAuth2User oauthUser = (DefaultOAuth2User)authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info("{} => {}", key, value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(false);
        user.setEnabled(true);

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePicture(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout(authorizedClientRegistrationId+"Hey there! I am New Here");
        }
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String email = oauthUser.getAttribute("email") != null
            ? oauthUser.getAttribute("email").toString()
            : oauthUser.getAttribute("login").toString()+"@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();


            user.setEmail(email);
            user.setProfilePicture(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
            user.setPassword("dummy");
            user.setAbout(providerUserId+" account created using github");
        }
        else {
            logger.info("Unknown provider");
        }


        response.sendRedirect("/user/dashboard");
        // google attributes



        // github attributes
        
        
        // facebook attributes


        
        // //database me user save

        // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(user.getName());

        // user.getAttributes().forEach((key, value) -> {
        //     logger.info("{} => {}", key, value);
        // });

        // logger.info(user.getAuthorities().toString());

        // String email=user.getAttribute("email").toString();
        // String name=user.getAttribute("name").toString();
        // String picture=user.getAttribute("picture").toString();

        // // create user in database

        // User user1=new User();
        // user1.setName(name);
        // user1.setEmail(email);
        // user1.setPassword("password");
        // user1.setProfilePicture(picture);
        // user1.setUserId(UUID.randomUUID().toString());
        // user1.setProvider(Providers.GOOGLE);
        // user1.setEnabled(true);

        // user1.setEmailVerified(true);
        // user1.setProviderUserId(user.getName());
        // user1.setRoleList(List.of("ROLE_USER"));
        // user1.setAbout("This account is created using google..");


        // User user2=userRepo.findByEmail(email).orElse(null);

        // if(user2==null){

        //     userRepo.save(user1);
        //     logger.info("User saved:" + email);
        // }


        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user2==null){
            userRepo.save(user);
            System.out.println("User saved:" + user.getEmail());
        }
        // new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}