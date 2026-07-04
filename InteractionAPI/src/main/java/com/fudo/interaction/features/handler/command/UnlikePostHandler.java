package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.UnlikePostCommand;
import com.fudo.interaction.interfaces.LikeRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class UnlikePostHandler implements RequestHandler<UnlikePostCommand, Void> {

    private final LikeRepository likes;

    public UnlikePostHandler(LikeRepository likes) {
        this.likes = likes;
    }

    @Override
    public Void handle(UnlikePostCommand cmd) {
        likes.unlike(cmd.getPostId(), cmd.getUserId());
        return null;
    }
}
