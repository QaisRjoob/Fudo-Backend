package com.fudo.identity.application.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fudo.shared.cqrs.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetUserQuery implements Request<GetUserQuery.UserDto> {

    private UUID userId;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserDto {
        private UUID id;
        private String email;
        private String username;
        private String displayName;
        private String bio;
        private String avatarUrl;
        private String gender;
        @JsonProperty("isActive")
        private boolean isActive;

        @JsonProperty("isAdmin")
        private boolean isAdmin;
        private Instant createdAt;
    }
}
