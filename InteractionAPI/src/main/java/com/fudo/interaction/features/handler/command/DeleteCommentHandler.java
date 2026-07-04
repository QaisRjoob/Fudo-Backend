package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.DeleteCommentCommand;
import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.interfaces.CommentRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DeleteCommentHandler implements RequestHandler<DeleteCommentCommand, Void> {

    private final CommentRepository comments;

    public DeleteCommentHandler(CommentRepository comments) {
        this.comments = comments;
    }

    @Override
    public Void handle(DeleteCommentCommand cmd) {
        PostComment comment = comments.getById(cmd.getCommentId())
                .orElseThrow(() -> new NotFoundException("Comment", cmd.getCommentId()));

        if (!cmd.isAdmin() && !comment.getAuthorId().equals(cmd.getRequestingUserId())) {
            throw new ForbiddenException("You can only delete your own comments.");
        }

        comments.delete(cmd.getCommentId());
        return null;
    }
}
