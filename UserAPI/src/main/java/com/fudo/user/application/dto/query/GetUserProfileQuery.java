package com.fudo.user.application.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fudo.shared.cqrs.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GetUserProfileQuery implements Request<GetUserProfileQuery.UserProfileDto> {

    private UUID userId;
    private String username;
    private UUID requestingUserId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileDto {
        private UUID id;
        private String username;
        private String displayName;
        private String bio;
        private String avatarUrl;
        private String gender;
        private int followerCount;
        private int followingCount;
        private int postCount;
        @JsonProperty("isPrivate")
        private boolean isPrivate;

        @JsonProperty("isFollowing")
        private boolean isFollowing;

        @JsonProperty("isOwnProfile")
        private boolean isOwnProfile;
        private Instant createdAt;
    }
}
