package com.fudo.story.entity;

import com.fudo.story.util.GuidCsvConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "story_highlights")
public class StoryHighlight {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "cover_url")
    private String coverUrl;

    @Convert(converter = GuidCsvConverter.class)
    @Column(name = "story_ids")
    private List<UUID> storyIds = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
