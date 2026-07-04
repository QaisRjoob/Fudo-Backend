package com.fudo.identity.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCommand implements Request<RegisterCommand.RegisterResult> {

    @NotBlank
    @Email
    @Size(max = 256)
    private String email;

    @NotBlank
    @Size(max = 64)
    private String username;

    @NotBlank
    @Size(max = 128)
    private String displayName;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    @Size(max = 500)
    private String bio;

    @Size(max = 10)
    private String gender;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RegisterResult {
        private java.util.UUID userId;
        private String accessToken;
        private String refreshToken;
    }
}
