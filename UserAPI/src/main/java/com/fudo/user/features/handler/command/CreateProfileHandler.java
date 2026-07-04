package com.fudo.user.features.handler.command;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.user.application.dto.command.CreateProfileCommand;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateProfileHandler implements RequestHandler<CreateProfileCommand, Void> {

    private final UserProfileRepository profiles;

    public CreateProfileHandler(UserProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Override
    public Void handle(CreateProfileCommand cmd) {
        if (profiles.getById(cmd.getUserId()).isPresent()) {
            return null;
        }

        UserProfile profile = new UserProfile();
        profile.setId(cmd.getUserId());
        profile.setUsername(cmd.getUsername().toLowerCase().trim());
        profile.setDisplayName(cmd.getDisplayName().trim());
        profile.setGender(cmd.getGender());

        profiles.add(profile);
        return null;
    }
}
