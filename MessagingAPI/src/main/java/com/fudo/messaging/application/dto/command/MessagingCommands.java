package com.fudo.messaging.application.dto.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Mirrors MessagingCommands.cs - keeps every command/result class co-located in one file like the original. */
public final class MessagingCommands {

    private MessagingCommands() {
    }

    @Getter
    @Setter
    public static class SendMessageCommand implements Request<MessageDto> {
        private UUID conversationId;
        private UUID senderId;

        @NotBlank
        @Size(max = 2000)
        private String content;

        @Size(max = 16)
        private String messageType = "text";

        private String mediaUrl;
    }

    @Getter
    @Setter
    public static class CreateConversationCommand implements Request<ConversationDto> {
        private UUID requestingUserId;

        @NotNull
        private UUID otherUserId;
    }

    @Getter
    @Setter
    public static class CreateGroupConversationCommand implements Request<ConversationDto> {
        private UUID requestingUserId;
        private List<UUID> memberIds = new ArrayList<>();

        @NotBlank
        private String groupName;
    }

    @Getter
    @Setter
    public static class MarkMessagesReadCommand implements Request<Void> {
        private UUID conversationId;
        private UUID userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        private UUID id;
        private UUID conversationId;
        private UUID senderId;
        private String content;
        private String messageType;
        private String mediaUrl;
        private Instant createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationDto {
        private UUID id;

        /** @JsonProperty forces the wire key back to "isGroup" - Jackson would otherwise strip
         *  the "is" off the Lombok-generated isGroup() getter and emit "group". */
        @JsonProperty("isGroup")
        private boolean group;

        private String groupName;
        private List<UUID> participantIds = new ArrayList<>();
        private Instant lastMessageAt;
    }
}
