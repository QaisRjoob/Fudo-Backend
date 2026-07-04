package com.fudo.user.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateProfileCommand implements Request<Void> {

    private UUID userId;

    @NotBlank
    @Size(max = 128)
    private String displayName;

    @Size(max = 500)
    private String bio;

    private String avatarUrl;

    private Boolean isPrivate;
}
