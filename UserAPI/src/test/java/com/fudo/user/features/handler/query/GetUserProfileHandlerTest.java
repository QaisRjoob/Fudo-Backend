package com.fudo.user.features.handler.query;

import com.fudo.user.application.dto.query.GetUserProfileQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.FollowRepository;
import com.fudo.user.interfaces.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserProfileHandlerTest {

    @Mock
    private UserProfileRepository profiles;

    @Mock
    private FollowRepository follows;

    private GetUserProfileHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetUserProfileHandler(profiles, follows);
    }

    @Test
    void returnsNull_whenProfileNotFoundById() {
        UUID userId = UUID.randomUUID();
        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUserId(userId);
        query.setRequestingUserId(UUID.randomUUID());

        when(profiles.getById(userId)).thenReturn(Optional.empty());

        UserProfileDto result = handler.handle(query);

        assertThat(result).isNull();
    }

    @Test
    void returnsNull_whenProfileNotFoundByUsername() {
        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUsername("missing");
        query.setRequestingUserId(UUID.randomUUID());

        when(profiles.getByUsername("missing")).thenReturn(Optional.empty());

        UserProfileDto result = handler.handle(query);

        assertThat(result).isNull();
        verify(profiles, never()).getById(any());
    }

    @Test
    void isOwnProfileTrue_andIsFollowingNotChecked_whenViewingOwnProfile() {
        UUID userId = UUID.randomUUID();
        UserProfile profile = buildProfile(userId);

        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUserId(userId);
        query.setRequestingUserId(userId);

        when(profiles.getById(userId)).thenReturn(Optional.of(profile));

        UserProfileDto result = handler.handle(query);

        assertThat(result.isOwnProfile()).isTrue();
        assertThat(result.isFollowing()).isFalse();
        verify(follows, never()).isFollowing(any(), any());
    }

    @Test
    void mapsFieldsAndIsFollowing_whenViewingOtherProfile() {
        UUID profileId = UUID.randomUUID();
        UUID requestingUserId = UUID.randomUUID();
        UserProfile profile = buildProfile(profileId);

        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUserId(profileId);
        query.setRequestingUserId(requestingUserId);

        when(profiles.getById(profileId)).thenReturn(Optional.of(profile));
        when(follows.isFollowing(requestingUserId, profileId)).thenReturn(true);

        UserProfileDto result = handler.handle(query);

        assertThat(result.getId()).isEqualTo(profileId);
        assertThat(result.getUsername()).isEqualTo(profile.getUsername());
        assertThat(result.getDisplayName()).isEqualTo(profile.getDisplayName());
        assertThat(result.isFollowing()).isTrue();
        assertThat(result.isOwnProfile()).isFalse();
    }

    private UserProfile buildProfile(UUID id) {
        UserProfile profile = new UserProfile();
        profile.setId(id);
        profile.setUsername("johndoe");
        profile.setDisplayName("John Doe");
        profile.setBio("bio");
        profile.setFollowerCount(5);
        profile.setFollowingCount(3);
        profile.setPostCount(2);
        profile.setPrivate(false);
        profile.setCreatedAt(Instant.now());
        return profile;
    }

    private static UUID any() {
        return org.mockito.ArgumentMatchers.any(UUID.class);
    }
}
