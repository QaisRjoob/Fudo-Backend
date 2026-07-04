package com.fudo.identity.features.handler.command;

import com.fudo.identity.application.dto.command.ChangePasswordCommand;
import com.fudo.identity.entity.ApplicationUser;
import com.fudo.identity.interfaces.UserRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.NotFoundException;
import com.fudo.shared.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChangePasswordHandler implements RequestHandler<ChangePasswordCommand, Void> {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordHandler(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Void handle(ChangePasswordCommand cmd) {
        ApplicationUser user = users.getById(cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("User", cmd.getUserId()));

        if (!passwordEncoder.matches(cmd.getCurrentPassword(), user.getPasswordHash())) {
            throw new ValidationException("currentPassword", "Current password is incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(cmd.getNewPassword()));
        user.setUpdatedAt(Instant.now());
        users.update(user);

        return null;
    }
}
