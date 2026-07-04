package com.fudo.story.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReplyToStoryCommand implements Request<Void> {

    private UUID storyId;
    private UUID senderId;

    @NotBlank
    @Size(max = 500)
    private String content;
}
