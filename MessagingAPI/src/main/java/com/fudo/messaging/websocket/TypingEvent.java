package com.fudo.messaging.websocket;

public record TypingEvent(String conversationId, String userId) {
}
