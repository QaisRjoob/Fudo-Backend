package com.fudo.interaction.infrastructure.repository;

import com.fudo.interaction.entity.PostSave;
import com.fudo.interaction.interfaces.SaveRepository;
import com.fudo.interaction.repository.PostSaveJpaRepository;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaSaveRepository implements SaveRepository {

    private final PostSaveJpaRepository jpaRepository;

    public JpaSaveRepository(PostSaveJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean hasSaved(UUID postId, UUID userId) {
        return jpaRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    @Transactional
    public void save(UUID postId, UUID userId) {
        if (hasSaved(postId, userId)) {
            return;
        }
        PostSave entity = new PostSave();
        entity.setPostId(postId);
        entity.setUserId(userId);
        jpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void unsave(UUID postId, UUID userId) {
        jpaRepository.deleteByPostIdAndUserId(postId, userId);
    }

    @Override
    public PagedResult<UUID> getSavedPostIds(UUID userId, int page, int pageSize) {
        long total = jpaRepository.countByUserId(userId);
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UUID> items = jpaRepository.findByUserIdOrderByCreatedAtDesc(userId, pageRequest)
                .stream()
                .map(PostSave::getPostId)
                .collect(Collectors.toList());
        return new PagedResult<>(items, (int) total, page, pageSize);
    }
}
