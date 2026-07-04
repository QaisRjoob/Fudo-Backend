package com.fudo.post.features.handler.command;

import com.fudo.post.application.dto.command.DeletePostCommand;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
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
class DeletePostHandlerTest {

    @Mock
    private PostRepository posts;

    private DeletePostHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DeletePostHandler(posts);
    }

    @Test
    void handle_throwsNotFound_whenPostMissing() {
        UUID postId = UUID.randomUUID();
        when(posts.getById(postId)).thenReturn(Optional.empty());

        DeletePostCommand cmd = new DeletePostCommand(postId, UUID.randomUUID(), false);

        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(NotFoundException.class);
        verify(posts, never()).delete(any());
    }

    @Test
    void handle_throwsForbidden_whenNotOwnerAndNotAdmin() {
        UUID postId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID requestingUserId = UUID.randomUUID();

        Post post = new Post();
        post.setAuthorId(authorId);
        when(posts.getById(postId)).thenReturn(Optional.of(post));

        DeletePostCommand cmd = new DeletePostCommand(postId, requestingUserId, false);

        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(ForbiddenException.class);
        verify(posts, never()).delete(any());
    }

    @Test
    void handle_deletes_whenOwnerMatches() {
        UUID postId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        Post post = new Post();
        post.setAuthorId(authorId);
        when(posts.getById(postId)).thenReturn(Optional.of(post));

        DeletePostCommand cmd = new DeletePostCommand(postId, authorId, false);

        handler.handle(cmd);

        verify(posts).delete(postId);
    }

    @Test
    void handle_deletes_whenAdminEvenIfNotOwner() {
        UUID postId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID requestingUserId = UUID.randomUUID();

        Post post = new Post();
        post.setAuthorId(authorId);
        when(posts.getById(postId)).thenReturn(Optional.of(post));

        DeletePostCommand cmd = new DeletePostCommand(postId, requestingUserId, true);

        handler.handle(cmd);

        verify(posts).delete(postId);
    }

    private static UUID any() {
        return org.mockito.ArgumentMatchers.any(UUID.class);
    }
}
