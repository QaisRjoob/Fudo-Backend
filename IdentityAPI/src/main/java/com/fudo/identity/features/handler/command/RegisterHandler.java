package com.fudo.identity.features.handler.command;

import com.fudo.identity.application.dto.command.RegisterCommand;
import com.fudo.identity.application.dto.command.RegisterCommand.RegisterResult;
import com.fudo.identity.application.service.JwtService;
import com.fudo.identity.entity.ApplicationUser;
import com.fudo.identity.entity.RefreshToken;
import com.fudo.identity.interfaces.RefreshTokenRepository;
import com.fudo.identity.interfaces.UserRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.security.JwtProperties;
import com.fudo.shared.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class RegisterHandler implements RequestHandler<RegisterCommand, RegisterResult> {

    private final UserRepository users;
    private final RefreshTokenRepository tokens;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    public RegisterHandler(UserRepository users, RefreshTokenRepository tokens, JwtService jwtService,
                            JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.tokens = tokens;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResult handle(RegisterCommand cmd) {
        if (users.emailExists(cmd.getEmail())) {
            throw new ValidationException("email", "Email is already registered.");
        }
        if (users.usernameExists(cmd.getUsername())) {
            throw new ValidationException("username", "Username is already taken.");
        }

        ApplicationUser user = new ApplicationUser();
        user.setEmail(cmd.getEmail().toLowerCase().trim());
        user.setUsername(cmd.getUsername().toLowerCase().trim());
        user.setDisplayName(cmd.getDisplayName().trim());
        user.setPasswordHash(passwordEncoder.encode(cmd.getPassword()));
        user.setBio(cmd.getBio());
        user.setGender(cmd.getGender());

        user = users.add(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken();

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(jwtProperties.getRefreshTokenExpiryDays(), ChronoUnit.DAYS));
        tokens.add(refreshTokenEntity);

        return new RegisterResult(user.getId(), accessToken, refreshToken);
    }
}
