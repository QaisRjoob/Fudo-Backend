package com.fudo.interaction.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddCommentCommand implements Request<AddCommentCommand.AddCommentResult> {

    private UUID postId;
    private UUID authorId;
    private UUID parentCommentId;

    @NotBlank
    @Size(max = 1000)
    private String content;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCommentResult {
        private UUID commentId;
    }
}
