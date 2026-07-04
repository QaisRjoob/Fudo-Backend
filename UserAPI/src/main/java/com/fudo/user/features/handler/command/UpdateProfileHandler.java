package com.fudo.user.features.handler.command;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.NotFoundException;
import com.fudo.user.application.dto.command.UpdateProfileCommand;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UpdateProfileHandler implements RequestHandler<UpdateProfileCommand, Void> {

    private final UserProfileRepository profiles;

    public UpdateProfileHandler(UserProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Override
    public Void handle(UpdateProfileCommand cmd) {
        UserProfile profile = profiles.getById(cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("UserProfile", cmd.getUserId()));

        profile.setDisplayName(cmd.getDisplayName().trim());
        profile.setBio(cmd.getBio());
        if (cmd.getAvatarUrl() != null) {
            profile.setAvatarUrl(cmd.getAvatarUrl());
        }
        if (cmd.getIsPrivate() != null) {
            profile.setPrivate(cmd.getIsPrivate());
        }
        profile.setUpdatedAt(Instant.now());

        profiles.update(profile);
        return null;
    }
}
