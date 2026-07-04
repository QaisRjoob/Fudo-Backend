package com.fudo.post.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdatePostCommand implements Request<Void> {

    private UUID postId;

    private UUID requestingUserId;

    @NotBlank
    @Size(max = 256)
    private String title;

    @Size(max = 2000)
    private String description;

    @Size(max = 64)
    private String cookingTime;

    @Size(max = 32)
    private String difficulty;

    private int servingSize = 1;
}
