package com.fudo.identity.features.handler.command;

import com.fudo.identity.application.dto.command.LogoutCommand;
import com.fudo.identity.interfaces.RefreshTokenRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutHandler implements RequestHandler<LogoutCommand, Void> {

    private final RefreshTokenRepository tokens;

    public LogoutHandler(RefreshTokenRepository tokens) {
        this.tokens = tokens;
    }

    @Override
    public Void handle(LogoutCommand cmd) {
        tokens.revoke(cmd.getRefreshToken());
        return null;
    }
}
