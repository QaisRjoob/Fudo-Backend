package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.DeleteCommentCommand;
import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.interfaces.CommentRepository;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Mirrors DeleteCommentHandler.Handle from CommentHandlers.cs:
 *  1. comment must exist and not already be soft-deleted (GetByIdAsync filters IsDeleted)
 *  2. only the author or an admin may delete it
 *  3. deletion is a soft delete (sets IsDeleted = true), never a hard delete
 */
@ExtendWith(MockitoExtension.class)
class DeleteCommentHandlerTest {

    @Mock
    private CommentRepository commentRepository;

    @Test
    void delete_whenCommentDoesNotExist_throwsNotFound() {
        DeleteCommentHandler handler = new DeleteCommentHandler(commentRepository);
        UUID commentId = UUID.randomUUID();

        when(commentRepository.getById(commentId)).thenReturn(Optional.empty());

        DeleteCommentCommand cmd = new DeleteCommentCommand();
        cmd.setCommentId(commentId);
        cmd.setRequestingUserId(UUID.randomUUID());

        assertThrows(NotFoundException.class, () -> handler.handle(cmd));
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void delete_whenRequesterIsNotAuthorAndNotAdmin_throwsForbidden() {
        DeleteCommentHandler handler = new DeleteCommentHandler(commentRepository);
        UUID commentId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        PostComment comment = new PostComment();
        comment.setId(commentId);
        comment.setAuthorId(authorId);
        when(commentRepository.getById(commentId)).thenReturn(Optional.of(comment));

        DeleteCommentCommand cmd = new DeleteCommentCommand();
        cmd.setCommentId(commentId);
        cmd.setRequestingUserId(otherUserId);
        cmd.setAdmin(false);

        assertThrows(ForbiddenException.class, () -> handler.handle(cmd));
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void delete_whenRequesterIsAuthor_softDeletesComment() {
        DeleteCommentHandler handler = new DeleteCommentHandler(commentRepository);
        UUID commentId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        PostComment comment = new PostComment();
        comment.setId(commentId);
        comment.setAuthorId(authorId);
        when(commentRepository.getById(commentId)).thenReturn(Optional.of(comment));

        DeleteCommentCommand cmd = new DeleteCommentCommand();
        cmd.setCommentId(commentId);
        cmd.setRequestingUserId(authorId);
        cmd.setAdmin(false);

        handler.handle(cmd);

        verify(commentRepository, times(1)).delete(commentId);
    }

    @Test
    void delete_whenRequesterIsAdminButNotAuthor_softDeletesComment() {
        DeleteCommentHandler handler = new DeleteCommentHandler(commentRepository);
        UUID commentId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        PostComment comment = new PostComment();
        comment.setId(commentId);
        comment.setAuthorId(authorId);
        when(commentRepository.getById(commentId)).thenReturn(Optional.of(comment));

        DeleteCommentCommand cmd = new DeleteCommentCommand();
        cmd.setCommentId(commentId);
        cmd.setRequestingUserId(adminId);
        cmd.setAdmin(true);

        handler.handle(cmd);

        verify(commentRepository, times(1)).delete(commentId);
    }
}
