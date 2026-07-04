package com.fudo.messaging.controller;

import com.fudo.messaging.application.dto.command.MessagingCommands.ConversationDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.CreateConversationCommand;
import com.fudo.messaging.application.dto.command.MessagingCommands.MarkMessagesReadCommand;
import com.fudo.messaging.application.dto.command.MessagingCommands.MessageDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.SendMessageCommand;
import com.fudo.messaging.application.dto.query.MessagingQueries.GetConversationsQuery;
import com.fudo.messaging.application.dto.query.MessagingQueries.GetMessagesQuery;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messaging")
public class MessagingController {

    private final Mediator mediator;
    private final SimpMessagingTemplate messagingTemplate;

    public MessagingController(Mediator mediator, SimpMessagingTemplate messagingTemplate) {
        this.mediator = mediator;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "20") int pageSize) {
        GetConversationsQuery query = new GetConversationsQuery();
        query.setUserId(CurrentUser.require().userId());
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationDto> createConversation(@Valid @RequestBody CreateConversationCommand cmd) {
        cmd.setRequestingUserId(CurrentUser.require().userId());
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable UUID conversationId,
                                          @RequestParam(defaultValue = "30") int limit,
                                          @RequestParam(required = false) String cursor) {
        GetMessagesQuery query = new GetMessagesQuery();
        query.setConversationId(conversationId);
        query.setRequestingUserId(CurrentUser.require().userId());
        query.setLimit(limit);
        query.setCursor(cursor);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<MessageDto> sendMessage(@PathVariable UUID conversationId,
                                                   @Valid @RequestBody SendMessageCommand cmd) {
        cmd.setConversationId(conversationId);
        cmd.setSenderId(CurrentUser.require().userId());
        MessageDto result = mediator.send(cmd);

        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, result);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/conversations/{conversationId}/read")
    public ResponseEntity<Void> markRead(@PathVariable UUID conversationId) {
        MarkMessagesReadCommand cmd = new MarkMessagesReadCommand();
        cmd.setConversationId(conversationId);
        cmd.setUserId(CurrentUser.require().userId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }
}
