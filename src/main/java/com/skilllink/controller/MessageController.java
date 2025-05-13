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

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(java.time.Instant.now());
        }
        return messageRepository.save(message);
    }

    @GetMapping("/conversation")
    public List<Message> getMessages(
            @RequestParam String user1,
            @RequestParam String user2
    ) {
        return messageRepository.findConversationBetween(user1, user2);
    }

    @GetMapping("/conversations")
    public List<String> getConversations(@RequestParam String email) {
        return messageRepository.findDistinctChatPartners(email);
    }

    @GetMapping(value = "/previews", produces = "application/json")
    public List<ConversationPreview> getConversationPreviews(@RequestParam String email) {
        System.out.println("✅ Reached /previews endpoint for: " + email);

        List<Message> latestMessages = messageRepository.findLatestMessagesPerContact(email);
        System.out.println("🧾 Messages found: " + (latestMessages != null ? latestMessages.size() : 0));

        if (latestMessages == null || latestMessages.isEmpty()) {
            System.out.println("🚫 No messages found. Returning empty JSON list.");
            return List.of(); // return valid empty JSON array []
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

            System.out.println("➡️ Preview: " + otherEmail + " | Msg: " + msg.getContent());

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
