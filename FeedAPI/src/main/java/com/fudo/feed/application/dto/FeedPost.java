package com.fudo.feed.application.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Query-result shape returned in feed pages. Mirrors the original .NET
 * FeedAPI's {@code FeedPost} class (it lived under Core/Entities/models in
 * the .NET source, but it is never persisted - it is assembled in-memory by
 * the repository from the posts/post_tags/post_media tables) - so this is a
 * plain DTO, not a JPA entity.
 */
public class FeedPost {

    private UUID id;
    private UUID authorId;
    private String title;
    private String thumbnailUrl;
    private int likeCount;
    private int commentCount;
    private List<String> tags = new ArrayList<>();
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
