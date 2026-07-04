package com.fudo.story.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class CreateStoryCommand implements Request<CreateStoryCommand.CreateStoryResult> {

    private UUID authorId;

    @NotBlank
    private String mediaUrl;

    @Size(max = 16)
    private String mediaType = "image";

    @Size(max = 500)
    private String caption;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateStoryResult {
        private UUID storyId;
        private Instant expiresAt;
    }
}
