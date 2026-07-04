package com.fudo.shared.events;

import java.util.UUID;

public class UserFollowedEvent extends BaseIntegrationEvent {

    public static final String EVENT_TYPE = "user.user_followed";

    private final UUID followerId;
    private final UUID followeeId;

    public UserFollowedEvent(UUID followerId, UUID followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getFolloweeId() {
        return followeeId;
    }
}
