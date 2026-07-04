package com.fudo.user.features.handler.command;

import com.fudo.user.application.dto.command.UnfollowUserCommand;
import com.fudo.user.interfaces.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnfollowUserHandlerTest {

    @Mock
    private FollowRepository follows;

    private UnfollowUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UnfollowUserHandler(follows);
    }

    @Test
    void delegatesUnfollowToRepository() {
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();
        UnfollowUserCommand cmd = new UnfollowUserCommand(followerId, followeeId);

        Void result = handler.handle(cmd);

        assertThat(result).isNull();
        verify(follows).unfollow(followerId, followeeId);
    }
}
