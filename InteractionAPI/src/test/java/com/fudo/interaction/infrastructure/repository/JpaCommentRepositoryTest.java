package com.fudo.interaction.infrastructure.repository;

import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.repository.PostCommentJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Mirrors CommentRepository.DeleteAsync, which performs an
 * ExecuteUpdateAsync(s => s.SetProperty(c => c.IsDeleted, true)) - i.e. a
 * soft delete, never an actual row deletion.
 */
@ExtendWith(MockitoExtension.class)
class JpaCommentRepositoryTest {

    @Mock
    private PostCommentJpaRepository jpaRepository;

    @Test
    void delete_marksCommentAsDeleted_doesNotHardDelete() {
        JpaCommentRepository repository = new JpaCommentRepository(jpaRepository);
        UUID commentId = UUID.randomUUID();

        repository.delete(commentId);

        verify(jpaRepository, times(1)).markDeleted(commentId);
        verify(jpaRepository, never()).deleteById(any());
        verify(jpaRepository, never()).delete(any(PostComment.class));
    }
}
