package com.fudo.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

/**
 * Read-only mapping onto the "post_tags" table, owned and schema-managed by
 * PostAPI. Mirrors the original .NET FeedAPI's {@code PostTagReadModel}
 * mapped via {@code .ToTable("PostTags")}.
 */
@Entity
@Immutable
@Table(name = "post_tags")
public class PostTagReadModel {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "tag", nullable = false)
    private String tag;

    protected PostTagReadModel() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getPostId() {
        return postId;
    }

    public String getTag() {
        return tag;
    }
}
