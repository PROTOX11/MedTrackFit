package com.example.medtrackfit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.medtrackfit.services.impl.MeditationTimeDTO;
import com.example.medtrackfit.entities.User;
import com.example.medtrackfit.entities.UsersPerformance;
import com.example.medtrackfit.services.UserService;
import com.example.medtrackfit.repositories.usersPerformanceRepository; // Import the repository
import com.medtrackfit.helper.Helper;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/user")
public class UserController { // Removed incorrect generic type <usersPerformanceRepository>

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private usersPerformanceRepository usersPerformanceRepository; // Corrected to match interface name

    @ModelAttribute
    public void addLoggedinUserInformation(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            logger.info("User logged in: {}", username);
            User user = userService.getUserByEmail(username);
            if (user != null) {
                logger.info("Fetched user: {} with userId: {}", user.getEmail(), user.getUserId());
                model.addAttribute("LoggedInUser", user);
            } else {
                logger.warn("User not found for email: {}", username);
            }
        } else {
            logger.warn("Authentication is null");
        }
    }

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        logger.info("User dashboard accessed");
        return "user/dashboard";
    }

    @PostMapping("/{userId}/meditation")
    public ResponseEntity<String> updateMeditationTime(
            @PathVariable String userId,
            @RequestBody MeditationTimeDTO meditationTimeDTO,
            Authentication authentication) {
        logger.info("Received request to update meditation time for userId: {}, meditationTime: {}",
                userId, meditationTimeDTO.getMeditationTime());

        if (authentication == null) {
            logger.error("Authentication is null");
            return ResponseEntity.status(401).body("Unauthorized: Not logged in");
        }

        String loggedInUsername = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("Logged in username: {}", loggedInUsername);
        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        if (loggedInUser == null) {
            logger.error("Logged in user not found for email: {}", loggedInUsername);
            return ResponseEntity.status(401).body("Unauthorized: User not found");
        }

        logger.info("Logged in userId: {}", loggedInUser.getUserId());
        if (!loggedInUser.getUserId().equals(userId)) {
            logger.warn("Unauthorized attempt to update meditation time for userId: {} by user: {}",
                    userId, loggedInUser.getUserId());
            return ResponseEntity.status(403).body("Unauthorized: You can only update your own meditation time");
        }

        try {
            userService.updateMeditationTime(userId, meditationTimeDTO.getMeditationTime());
            logger.info("Meditation time updated successfully for userId: {}", userId);
            return ResponseEntity.ok("Meditation time updated successfully");
        } catch (Exception e) {
            logger.error("Error updating meditation time for userId: {}", userId, e);
            return ResponseEntity.status(500).body("Error updating meditation time: " + e.getMessage());
        }
    }

    @GetMapping("/performance/{userId}")
    public ResponseEntity<UsersPerformance> getUserPerformance(
            @PathVariable String userId,
            Authentication authentication) {
        logger.info("Received request to fetch performance for userId: {}", userId);
        if (authentication == null) {
            logger.error("Authentication is null");
            return ResponseEntity.status(401).body(null);
        }

        String loggedInUsername = Helper.getEmailOfLoggedInUser(authentication);
        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        if (loggedInUser == null) {
            logger.error("Logged in user not found for email: {}", loggedInUsername);
            return ResponseEntity.status(401).body(null);
        }

        if (!loggedInUser.getUserId().equals(userId)) {
            logger.warn("Unauthorized attempt to fetch performance for userId: {} by user: {}",
                    userId, loggedInUser.getUserId());
            return ResponseEntity.status(403).body(null);
        }

        try {
            UsersPerformance performance = usersPerformanceRepository.findByUserUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Performance data not found for user: " + userId));
            logger.info("Fetched performance for userId: {}: meditationScore: {}, hydrationScore: {}", 
                    userId, performance.getMeditationScore(), performance.getHydrationScore());
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            logger.error("Error fetching performance for userId: {}", userId, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/{userId}/hydration")
    public ResponseEntity<Void> updateHydration(
            @PathVariable String userId,
            @RequestBody Map<String, Integer> payload,
            Authentication authentication) {
        logger.info("Received request to update hydration for userId: {}", userId);

        if (authentication == null) {
            logger.error("Authentication is null");
            return ResponseEntity.status(401).build();
        }

        String loggedInUsername = Helper.getEmailOfLoggedInUser(authentication);
        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        if (loggedInUser == null) {
            logger.error("Logged in user not found for email: {}", loggedInUsername);
            return ResponseEntity.status(401).build();
        }

        if (!loggedInUser.getUserId().equals(userId)) {
            logger.warn("Unauthorized attempt to update hydration for userId: {} by user: {}",
                    userId, loggedInUser.getUserId());
            return ResponseEntity.status(403).build();
        }

        try {
            Integer hydrationAmount = payload.get("hydrationAmount");
            if (hydrationAmount == null || hydrationAmount < 0) {
                logger.warn("Invalid hydration amount: {}", hydrationAmount);
                return ResponseEntity.badRequest().build();
            }

            UsersPerformance performance = usersPerformanceRepository.findByUserUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Performance data not found for user: " + userId));
            performance.setHydrationScore(performance.getHydrationScore() + hydrationAmount);
            usersPerformanceRepository.save(performance);
            logger.info("Hydration updated successfully for userId: {}, new hydrationScore: {}", 
                    userId, performance.getHydrationScore());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error updating hydration for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
        User user = userService.getUserByEmail(username);
        logger.info("User name: {}, email: {}", user.getName(), user.getEmail());
        model.addAttribute("loggedInUser", user);
        return "user/profile";
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            Authentication authentication,
            Model model) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        try {
            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Only image files are allowed");
                    model.addAttribute("loggedInUser", user);
                    return "user/profile";
                }

                userService.updateProfilePicture((user.getUserId()), file);
                logger.info("Profile picture updated for user: {}", username);
            } else {
                model.addAttribute("error", "No file uploaded");
                model.addAttribute("loggedInUser", user);
                return "user/profile";
            }
        } catch (Exception e) {
            logger.error("Failed to update profile picture for user: {}", username, e);
            model.addAttribute("error", "Failed to update profile picture: ");
            model.addAttribute("loggedInUser", user);
            return "user/profile";
        }

        return "redirect:/user/profile";
    }

    @RequestMapping("/settings")
    public String settings() {
        return "user/settings";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "user/logout";
    }

    @RequestMapping("/chat")
    public String chat() {
        return "user/chat";
    }

    @RequestMapping("/recpatient")
    public String recpatient() {
        return "user/recpatient";
    }
}