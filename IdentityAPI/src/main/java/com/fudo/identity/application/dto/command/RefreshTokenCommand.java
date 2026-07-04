package com.fudo.identity.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenCommand implements Request<RefreshTokenCommand.RefreshTokenResult> {

    @NotBlank
    private String refreshToken;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RefreshTokenResult {
        private String accessToken;
        private String refreshToken;
    }
}
