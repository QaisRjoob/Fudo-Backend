package com.fudo.interaction.application.dto.query;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    private UUID id;
    private UUID postId;
    private UUID authorId;
    private UUID parentCommentId;
    private String content;
    private int likeCount;
    private Instant createdAt;
    private List<CommentDto> replies = new ArrayList<>();
}
