package com.fudo.identity.features.handler.command;

import com.fudo.identity.application.dto.command.LoginCommand;
import com.fudo.identity.application.dto.command.LoginCommand.LoginResult;
import com.fudo.identity.application.service.JwtService;
import com.fudo.identity.entity.ApplicationUser;
import com.fudo.identity.entity.RefreshToken;
import com.fudo.identity.interfaces.RefreshTokenRepository;
import com.fudo.identity.interfaces.UserRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.security.JwtProperties;
import com.fudo.shared.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class LoginHandler implements RequestHandler<LoginCommand, LoginResult> {

    private final UserRepository users;
    private final RefreshTokenRepository tokens;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    public LoginHandler(UserRepository users, RefreshTokenRepository tokens, JwtService jwtService,
                         JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.tokens = tokens;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResult handle(LoginCommand cmd) {
        ApplicationUser user = users.getByEmail(cmd.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

        if (!user.isActive()) {
            throw new UnauthorizedException("Account is disabled.");
        }

        if (!passwordEncoder.matches(cmd.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken();

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(jwtProperties.getRefreshTokenExpiryDays(), ChronoUnit.DAYS));
        tokens.add(refreshTokenEntity);

        return new LoginResult(user.getId(), user.getUsername(), user.getDisplayName(), user.getAvatarUrl(),
                accessToken, refreshToken);
    }
}
