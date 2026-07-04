package com.fudo.user.features.handler.command;

import com.fudo.shared.exception.NotFoundException;
import com.fudo.shared.exception.ValidationException;
import com.fudo.user.application.dto.command.FollowUserCommand;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.FollowRepository;
import com.fudo.user.interfaces.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowUserHandlerTest {

    @Mock
    private FollowRepository follows;

    @Mock
    private UserProfileRepository profiles;

    private FollowUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FollowUserHandler(follows, profiles);
    }

    @Test
    void throwsValidationException_whenFollowingSelf() {
        UUID userId = UUID.randomUUID();
        FollowUserCommand cmd = new FollowUserCommand(userId, userId);

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(ValidationException.class);

        verify(profiles, never()).getById(userId);
    }

    @Test
    void throwsNotFoundException_whenFolloweeDoesNotExist() {
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();
        FollowUserCommand cmd = new FollowUserCommand(followerId, followeeId);

        when(profiles.getById(followeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void doesNothing_whenAlreadyFollowing() {
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();
        FollowUserCommand cmd = new FollowUserCommand(followerId, followeeId);

        UserProfile followee = new UserProfile();
        followee.setId(followeeId);

        when(profiles.getById(followeeId)).thenReturn(Optional.of(followee));
        when(follows.isFollowing(followerId, followeeId)).thenReturn(true);

        Void result = handler.handle(cmd);

        assertThat(result).isNull();
        verify(follows, never()).follow(followerId, followeeId);
    }

    @Test
    void follows_whenNotAlreadyFollowingAndFolloweeExists() {
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();
        FollowUserCommand cmd = new FollowUserCommand(followerId, followeeId);

        UserProfile followee = new UserProfile();
        followee.setId(followeeId);

        when(profiles.getById(followeeId)).thenReturn(Optional.of(followee));
        when(follows.isFollowing(followerId, followeeId)).thenReturn(false);

        handler.handle(cmd);

        verify(follows).follow(followerId, followeeId);
    }
}
