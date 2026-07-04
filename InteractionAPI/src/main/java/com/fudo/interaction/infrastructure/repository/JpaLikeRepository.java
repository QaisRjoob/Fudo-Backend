package com.fudo.interaction.infrastructure.repository;

import com.fudo.interaction.entity.PostLike;
import com.fudo.interaction.interfaces.LikeRepository;
import com.fudo.interaction.repository.PostLikeJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaLikeRepository implements LikeRepository {

    private final PostLikeJpaRepository jpaRepository;

    public JpaLikeRepository(PostLikeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean hasLiked(UUID postId, UUID userId) {
        return jpaRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    @Transactional
    public void like(UUID postId, UUID userId) {
        if (hasLiked(postId, userId)) {
            return;
        }
        PostLike like = new PostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        jpaRepository.save(like);
    }

    @Override
    @Transactional
    public void unlike(UUID postId, UUID userId) {
        jpaRepository.deleteByPostIdAndUserId(postId, userId);
    }

    @Override
    public int getLikeCount(UUID postId) {
        return (int) jpaRepository.countByPostId(postId);
    }

    @Override
    public List<UUID> getLikerIds(UUID postId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return jpaRepository.findByPostIdOrderByCreatedAtDesc(postId, pageRequest)
                .stream()
                .map(PostLike::getUserId)
                .collect(Collectors.toList());
    }
}
