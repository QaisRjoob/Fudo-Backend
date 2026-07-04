package com.fudo.shared.events;

import java.util.UUID;

public class StoryCreatedEvent extends BaseIntegrationEvent {

    public static final String EVENT_TYPE = "story.story_created";

    private final UUID storyId;
    private final UUID authorId;

    public StoryCreatedEvent(UUID storyId, UUID authorId) {
        this.storyId = storyId;
        this.authorId = authorId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public UUID getStoryId() {
        return storyId;
    }

    public UUID getAuthorId() {
        return authorId;
    }
}
