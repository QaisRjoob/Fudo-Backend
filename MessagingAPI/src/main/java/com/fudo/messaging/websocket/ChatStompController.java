package com.fudo.messaging.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Equivalent of ChatHub.SendTyping. Clients SEND to /app/conversations/{id}/typing;
 * everyone subscribed to /topic/conversations/{id} (including the sender) receives
 * it - the original used Clients.OthersInGroup to exclude the sender, which STOMP's
 * simple broker has no built-in equivalent for, so the client should ignore
 * TypingEvents where userId equals its own id. This is the one intentional
 * behavioral simplification in the WebSocket port.
 */
@Controller
public class ChatStompController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatStompController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/conversations/{conversationId}/typing")
    public void sendTyping(@DestinationVariable String conversationId, Principal principal) {
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId,
                new TypingEvent(conversationId, principal.getName()));
    }
}
