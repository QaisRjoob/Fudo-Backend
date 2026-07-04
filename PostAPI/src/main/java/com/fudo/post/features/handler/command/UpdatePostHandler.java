package com.fudo.post.features.handler.command;

import com.fudo.post.application.dto.command.UpdatePostCommand;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UpdatePostHandler implements RequestHandler<UpdatePostCommand, Void> {

    private final PostRepository posts;

    public UpdatePostHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public Void handle(UpdatePostCommand cmd) {
        Post post = posts.getById(cmd.getPostId())
                .orElseThrow(() -> new NotFoundException("Post", cmd.getPostId()));

        if (!post.getAuthorId().equals(cmd.getRequestingUserId())) {
            throw new ForbiddenException("You can only edit your own posts.");
        }

        post.setTitle(cmd.getTitle().trim());
        post.setDescription(cmd.getDescription());
        post.setCookingTime(cmd.getCookingTime());
        post.setDifficulty(cmd.getDifficulty());
        post.setServingSize(cmd.getServingSize());
        post.setUpdatedAt(Instant.now());

        posts.update(post);
        return null;
    }
}
