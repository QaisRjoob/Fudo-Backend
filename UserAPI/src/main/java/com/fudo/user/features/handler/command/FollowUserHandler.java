package com.fudo.user.features.handler.command;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.NotFoundException;
import com.fudo.shared.exception.ValidationException;
import com.fudo.user.application.dto.command.FollowUserCommand;
import com.fudo.user.interfaces.FollowRepository;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

@Component
public class FollowUserHandler implements RequestHandler<FollowUserCommand, Void> {

    private final FollowRepository follows;
    private final UserProfileRepository profiles;

    public FollowUserHandler(FollowRepository follows, UserProfileRepository profiles) {
        this.follows = follows;
        this.profiles = profiles;
    }

    @Override
    public Void handle(FollowUserCommand cmd) {
        if (cmd.getFollowerId().equals(cmd.getFolloweeId())) {
            throw new ValidationException("followeeId", "Cannot follow yourself.");
        }

        profiles.getById(cmd.getFolloweeId())
                .orElseThrow(() -> new NotFoundException("User", cmd.getFolloweeId()));

        if (follows.isFollowing(cmd.getFollowerId(), cmd.getFolloweeId())) {
            return null;
        }

        follows.follow(cmd.getFollowerId(), cmd.getFolloweeId());
        return null;
    }
}
