package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.ChatMessage;
import com.example.medtrackfit.repositories.ChatMessageRepository;
import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UniversalUserService universalUserService;

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam("recipient") String recipient,
                                         Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String currentUserEmail = Helper.getEmailOfLoggedInUser(authentication);

        List<ChatMessage> history;
        if (recipient.toLowerCase().contains("group") || recipient.toLowerCase().contains("community")) {
            history = chatMessageRepository.findGroupHistory(recipient);
        } else {
            history = chatMessageRepository.findChatHistory(currentUserEmail, recipient);
        }

        return ResponseEntity.ok(history);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> body,
                                         Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String currentUserEmail = Helper.getEmailOfLoggedInUser(authentication);
        String currentUserName = universalUserService.getUserName(currentUserEmail);

        String recipient = body.get("recipientEmail");
        String text = body.get("text");

        if (recipient == null || text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Recipient and text are required"));
        }

        ChatMessage message = ChatMessage.builder()
                .senderEmail(currentUserEmail)
                .senderName(currentUserName)
                .recipientEmail(recipient)
                .text(text)
                .timestamp(LocalDateTime.now())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);
        return ResponseEntity.ok(saved);
    }
}
