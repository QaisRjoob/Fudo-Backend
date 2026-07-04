package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.LikePostCommand;
import com.fudo.interaction.interfaces.LikeRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class LikePostHandler implements RequestHandler<LikePostCommand, Void> {

    private final LikeRepository likes;

    public LikePostHandler(LikeRepository likes) {
        this.likes = likes;
    }

    @Override
    public Void handle(LikePostCommand cmd) {
        likes.like(cmd.getPostId(), cmd.getUserId());
        return null;
    }
}
