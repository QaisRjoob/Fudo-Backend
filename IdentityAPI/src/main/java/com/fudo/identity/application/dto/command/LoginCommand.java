package com.fudo.identity.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginCommand implements Request<LoginCommand.LoginResult> {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoginResult {
        private UUID userId;
        private String username;
        private String displayName;
        private String avatarUrl;
        private String accessToken;
        private String refreshToken;
    }
}
