package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.AddCommentCommand;
import com.fudo.interaction.application.dto.command.AddCommentCommand.AddCommentResult;
import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.interfaces.CommentRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class AddCommentHandler implements RequestHandler<AddCommentCommand, AddCommentResult> {

    private final CommentRepository comments;

    public AddCommentHandler(CommentRepository comments) {
        this.comments = comments;
    }

    @Override
    public AddCommentResult handle(AddCommentCommand cmd) {
        PostComment comment = new PostComment();
        comment.setPostId(cmd.getPostId());
        comment.setAuthorId(cmd.getAuthorId());
        comment.setParentCommentId(cmd.getParentCommentId());
        comment.setContent(cmd.getContent().trim());

        comment = comments.add(comment);
        return new AddCommentResult(comment.getId());
    }
}
