package com.fudo.post.features.handler.command;

import com.fudo.post.application.dto.command.DeletePostCommand;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DeletePostHandler implements RequestHandler<DeletePostCommand, Void> {

    private final PostRepository posts;

    public DeletePostHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public Void handle(DeletePostCommand cmd) {
        Post post = posts.getById(cmd.getPostId())
                .orElseThrow(() -> new NotFoundException("Post", cmd.getPostId()));

        if (!cmd.isAdmin() && !post.getAuthorId().equals(cmd.getRequestingUserId())) {
            throw new ForbiddenException("You can only delete your own posts.");
        }

        posts.delete(cmd.getPostId());
        return null;
    }
}
