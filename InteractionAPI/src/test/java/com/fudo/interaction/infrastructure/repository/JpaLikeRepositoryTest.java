package com.fudo.interaction.infrastructure.repository;

import com.fudo.interaction.entity.PostLike;
import com.fudo.interaction.repository.PostLikeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Mirrors the original LikeRepository.LikeAsync: "if (await HasLikedAsync(...)) return;"
 * i.e. liking an already-liked post must silently no-op rather than throwing or
 * inserting a duplicate row.
 */
@ExtendWith(MockitoExtension.class)
class JpaLikeRepositoryTest {

    @Mock
    private PostLikeJpaRepository jpaRepository;

    @Test
    void like_whenAlreadyLiked_doesNotInsertAgain() {
        JpaLikeRepository repository = new JpaLikeRepository(jpaRepository);
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(jpaRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

        repository.like(postId, userId);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    void like_whenNotYetLiked_insertsNewLike() {
        JpaLikeRepository repository = new JpaLikeRepository(jpaRepository);
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(jpaRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);
        when(jpaRepository.save(any(PostLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

        repository.like(postId, userId);

        ArgumentCaptor<PostLike> captor = ArgumentCaptor.forClass(PostLike.class);
        verify(jpaRepository, times(1)).save(captor.capture());
        assertEquals(postId, captor.getValue().getPostId());
        assertEquals(userId, captor.getValue().getUserId());
    }

    @Test
    void unlike_delegatesDeleteToJpaRepository() {
        JpaLikeRepository repository = new JpaLikeRepository(jpaRepository);
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        repository.unlike(postId, userId);

        verify(jpaRepository, times(1)).deleteByPostIdAndUserId(postId, userId);
    }
}
