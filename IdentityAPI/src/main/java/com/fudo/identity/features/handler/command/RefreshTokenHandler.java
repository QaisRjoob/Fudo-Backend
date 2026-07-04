package com.fudo.identity.features.handler.command;

import com.fudo.identity.application.dto.command.RefreshTokenCommand;
import com.fudo.identity.application.dto.command.RefreshTokenCommand.RefreshTokenResult;
import com.fudo.identity.application.service.JwtService;
import com.fudo.identity.entity.RefreshToken;
import com.fudo.identity.interfaces.RefreshTokenRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.security.JwtProperties;
import com.fudo.shared.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class RefreshTokenHandler implements RequestHandler<RefreshTokenCommand, RefreshTokenResult> {

    private final RefreshTokenRepository tokens;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public RefreshTokenHandler(RefreshTokenRepository tokens, JwtService jwtService, JwtProperties jwtProperties) {
        this.tokens = tokens;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public RefreshTokenResult handle(RefreshTokenCommand cmd) {
        RefreshToken stored = tokens.getByToken(cmd.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token."));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token is expired or revoked.");
        }

        tokens.revoke(cmd.getRefreshToken());

        String newAccessToken = jwtService.generateAccessToken(stored.getUser());
        String newRefreshToken = jwtService.generateRefreshToken();

        RefreshToken newRefreshTokenEntity = new RefreshToken();
        newRefreshTokenEntity.setUserId(stored.getUserId());
        newRefreshTokenEntity.setToken(newRefreshToken);
        newRefreshTokenEntity.setExpiresAt(Instant.now().plus(jwtProperties.getRefreshTokenExpiryDays(), ChronoUnit.DAYS));
        tokens.add(newRefreshTokenEntity);

        return new RefreshTokenResult(newAccessToken, newRefreshToken);
    }
}
