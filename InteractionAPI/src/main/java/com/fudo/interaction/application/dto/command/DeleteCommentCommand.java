package com.fudo.interaction.application.dto.command;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteCommentCommand implements Request<Void> {
    private UUID commentId;
    private UUID requestingUserId;
    private boolean isAdmin;
}
