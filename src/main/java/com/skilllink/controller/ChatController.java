package com.skilllink.controller;

import com.skilllink.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
/**
 * Handles WebSocket messages for chat functionality.
 */
@Controller
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        message.setTimestamp(java.time.Instant.now());
        return message;
    }
}
