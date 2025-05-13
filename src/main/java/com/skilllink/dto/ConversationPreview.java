package com.skilllink.dto;

import java.time.Instant;

public class ConversationPreview {
    private String otherUserEmail;
    private String otherUserName;
    private String lastMessage;
    private Instant timestamp;
    private String profilePictureUrl; // ✅ NEW

    public ConversationPreview(String email, String name, String lastMessage, Instant timestamp, String profilePictureUrl) {
        this.otherUserEmail = email;
        this.otherUserName = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getOtherUserEmail() {
        return otherUserEmail;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
