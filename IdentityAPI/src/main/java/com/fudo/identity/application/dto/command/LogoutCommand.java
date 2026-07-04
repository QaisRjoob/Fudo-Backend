package com.fudo.identity.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutCommand implements Request<Void> {

    @NotBlank
    private String refreshToken;
}
