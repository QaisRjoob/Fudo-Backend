package com.fudo.shared.events;

import java.util.UUID;

public class PostLikedEvent extends BaseIntegrationEvent {

    public static final String EVENT_TYPE = "interaction.post_liked";

    private final UUID postId;
    private final UUID authorId;
    private final UUID likedByUserId;

    public PostLikedEvent(UUID postId, UUID authorId, UUID likedByUserId) {
        this.postId = postId;
        this.authorId = authorId;
        this.likedByUserId = likedByUserId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public UUID getPostId() {
        return postId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getLikedByUserId() {
        return likedByUserId;
    }
}
