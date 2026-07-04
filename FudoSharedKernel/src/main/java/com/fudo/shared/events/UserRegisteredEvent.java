package com.fudo.shared.events;

import java.util.UUID;

public class UserRegisteredEvent extends BaseIntegrationEvent {

    public static final String EVENT_TYPE = "identity.user_registered";

    private final UUID userId;
    private final String email;
    private final String username;
    private final String displayName;
    private final String gender;

    public UserRegisteredEvent(UUID userId, String email, String username, String displayName, String gender) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.displayName = displayName;
        this.gender = gender;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGender() {
        return gender;
    }
}
