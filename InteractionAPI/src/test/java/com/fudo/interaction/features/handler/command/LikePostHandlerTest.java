package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.LikePostCommand;
import com.fudo.interaction.application.dto.command.UnlikePostCommand;
import com.fudo.interaction.interfaces.LikeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * The actual idempotency decision (no-op if already liked) lives in
 * JpaLikeRepository.like(), mirroring the original LikeRepository.LikeAsync.
 * These tests cover the handler's contract: it always delegates to the
 * repository, which owns the idempotency behavior - same shape as
 * LikePostHandler.Handle in the .NET source, which has no logic of its own
 * beyond forwarding to ILikeRepository.LikeAsync/UnlikeAsync.
 */
@ExtendWith(MockitoExtension.class)
class LikePostHandlerTest {

    @Mock
    private LikeRepository likeRepository;

    @Test
    void like_delegatesToRepository() {
        LikePostHandler handler = new LikePostHandler(likeRepository);
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LikePostCommand cmd = new LikePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(userId);

        handler.handle(cmd);

        verify(likeRepository, times(1)).like(postId, userId);
    }

    @Test
    void unlike_delegatesToRepository() {
        UnlikePostHandler handler = new UnlikePostHandler(likeRepository);
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UnlikePostCommand cmd = new UnlikePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(userId);

        handler.handle(cmd);

        verify(likeRepository, times(1)).unlike(postId, userId);
    }
}
