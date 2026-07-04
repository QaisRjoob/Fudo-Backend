package com.fudo.identity.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChangePasswordCommand implements Request<Void> {

    /** Populated by the controller from the authenticated principal, not bound from the request body. */
    private UUID userId;

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 128)
    private String newPassword;
}
