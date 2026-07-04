package com.fudo.shared.events;

import java.util.UUID;

public class PostCreatedEvent extends BaseIntegrationEvent {

    public static final String EVENT_TYPE = "post.post_created";

    private final UUID postId;
    private final UUID authorId;
    private final String title;

    public PostCreatedEvent(UUID postId, UUID authorId, String title) {
        this.postId = postId;
        this.authorId = authorId;
        this.title = title;
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

    public String getTitle() {
        return title;
    }
}
