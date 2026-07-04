package com.fudo.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

/**
 * Read-only mapping onto the "post_media" table, owned and schema-managed by
 * PostAPI. Mirrors the original .NET FeedAPI's {@code PostMediaReadModel}
 * mapped via {@code .ToTable("PostMedia")}.
 */
@Entity
@Immutable
@Table(name = "post_media")
public class PostMediaReadModel {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected PostMediaReadModel() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getPostId() {
        return postId;
    }

    public String getUrl() {
        return url;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
