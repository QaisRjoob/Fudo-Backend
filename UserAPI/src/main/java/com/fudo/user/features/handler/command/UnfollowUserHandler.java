package com.fudo.user.features.handler.command;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.user.application.dto.command.UnfollowUserCommand;
import com.fudo.user.interfaces.FollowRepository;
import org.springframework.stereotype.Component;

@Component
public class UnfollowUserHandler implements RequestHandler<UnfollowUserCommand, Void> {

    private final FollowRepository follows;

    public UnfollowUserHandler(FollowRepository follows) {
        this.follows = follows;
    }

    @Override
    public Void handle(UnfollowUserCommand cmd) {
        follows.unfollow(cmd.getFollowerId(), cmd.getFolloweeId());
        return null;
    }
}
