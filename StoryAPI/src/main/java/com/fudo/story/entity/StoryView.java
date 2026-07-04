package com.fudo.story.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "story_views",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_story_views_story_id_viewer_id", columnNames = {"story_id", "viewer_id"})
        })
public class StoryView {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "story_id", nullable = false)
    private UUID storyId;

    @Column(name = "viewer_id", nullable = false)
    private UUID viewerId;

    @Column(name = "viewed_at", nullable = false)
    private Instant viewedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", insertable = false, updatable = false)
    private Story story;
}
