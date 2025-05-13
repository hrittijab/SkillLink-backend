package com.skilllink.controller;

import com.skilllink.dto.ConversationPreview;
import com.skilllink.model.Message;
import com.skilllink.model.User;
import com.skilllink.repository.MessageRepository;
import com.skilllink.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing chat messages and conversations.
 * Provides endpoints for sending messages, retrieving chat history, and showing conversation previews.
 *
 * Author: Hrittija Bhattacharjee
 */
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves a new message sent between users.
     */
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(java.time.Instant.now());
        }
        return messageRepository.save(message);
    }

    /**
     * Retrieves all messages exchanged between two users.
     */
    @GetMapping("/conversation")
    public List<Message> getMessages(
            @RequestParam String user1,
            @RequestParam String user2
    ) {
        return messageRepository.findConversationBetween(user1, user2);
    }

    /**
     * Returns a list of unique conversation partners for a given user.
     */
    @GetMapping("/conversations")
    public List<String> getConversations(@RequestParam String email) {
        return messageRepository.findDistinctChatPartners(email);
    }

    /**
     * Returns a list of the most recent message from each conversation for the given user.
     */
    @GetMapping(value = "/previews", produces = "application/json")
    public List<ConversationPreview> getConversationPreviews(@RequestParam String email) {
        List<Message> latestMessages = messageRepository.findLatestMessagesPerContact(email);

        if (latestMessages == null || latestMessages.isEmpty()) {
            return List.of(); 
        }

        return latestMessages.stream().map(msg -> {
            String otherEmail = msg.getSenderEmail().equalsIgnoreCase(email)
                    ? msg.getReceiverEmail()
                    : msg.getSenderEmail();

            User otherUser = userRepository.getUserByEmail(otherEmail);

            String fullName = (otherUser != null)
                    ? (otherUser.getFirstName() + " " + otherUser.getLastName()).trim()
                    : otherEmail;

            String profilePicture = (otherUser != null)
                    ? otherUser.getProfilePictureUrl()
                    : "";

            return new ConversationPreview(
                    otherEmail,
                    fullName,
                    msg.getContent(),
                    msg.getTimestamp(),
                    profilePicture
            );
        }).sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
        .collect(Collectors.toList());
    }
}
