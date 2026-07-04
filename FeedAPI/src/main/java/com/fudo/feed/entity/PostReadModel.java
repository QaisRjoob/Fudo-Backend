package com.fudo.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.util.UUID;

/**
 * Read-only mapping onto the "posts" table, which is owned and schema-managed
 * by PostAPI. FeedAPI has no tables of its own - it queries this table
 * directly (read-only) from the shared FudoDB database, mirroring the
 * original .NET FeedAPI's {@code PostReadModel} EF Core entity mapped via
 * {@code .ToTable("Posts")}. Only the columns FeedAPI actually needs are
 * mapped here; PostAPI's entity has additional columns this entity ignores.
 */
@Entity
@Immutable
@Table(name = "posts")
public class PostReadModel {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Column(name = "is_published", nullable = false)
    private boolean isPublished;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PostReadModel() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
