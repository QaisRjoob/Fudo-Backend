package com.fudo.story.application.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Shared response DTO — mirrors the single top-level {@code StoryDto} class in the
 * original C# source (declared alongside GetStoriesQuery, reused by GetStoryArchiveQuery).
 */
@Getter
@Setter
public class StoryDto {
    private UUID id;
    private String mediaUrl;
    private String mediaType;
    private String caption;

    @JsonProperty("isViewed")
    private boolean isViewed;
    private int viewCount;
    private Instant expiresAt;
    private Instant createdAt;
}
