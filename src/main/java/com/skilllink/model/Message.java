package com.skilllink.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a chat message between two users.
 * Each message contains sender and receiver emails, content, and a timestamp.
 *
 * Author: Hrittija Bhattacharjee
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the message

    private String senderEmail;      // Email of the user who sent the message
    private String receiverEmail;    // Email of the user who received the message
    private String content;          // Message content
    private Instant timestamp;       // Timestamp when the message was sent

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
