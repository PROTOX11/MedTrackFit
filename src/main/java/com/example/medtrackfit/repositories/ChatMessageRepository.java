package com.example.medtrackfit.repositories;

import com.example.medtrackfit.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT c FROM ChatMessage c WHERE " +
           "(c.senderEmail = :user1 AND c.recipientEmail = :user2) OR " +
           "(c.senderEmail = :user2 AND c.recipientEmail = :user1) " +
           "ORDER BY c.timestamp ASC")
    List<ChatMessage> findChatHistory(
            @Param("user1") String user1,
            @Param("user2") String user2
    );

    @Query("SELECT c FROM ChatMessage c WHERE c.recipientEmail = :groupName ORDER BY c.timestamp ASC")
    List<ChatMessage> findGroupHistory(@Param("groupName") String groupName);
}
