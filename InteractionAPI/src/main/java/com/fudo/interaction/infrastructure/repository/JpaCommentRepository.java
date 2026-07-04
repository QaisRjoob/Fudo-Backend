package com.fudo.interaction.infrastructure.repository;

import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.interfaces.CommentRepository;
import com.fudo.interaction.repository.PostCommentJpaRepository;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaCommentRepository implements CommentRepository {

    private final PostCommentJpaRepository jpaRepository;

    public JpaCommentRepository(PostCommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<PostComment> getById(UUID id) {
        return jpaRepository.findByIdAndIsDeletedFalse(id);
    }

    @Override
    @Transactional
    public PagedResult<PostComment> getByPost(UUID postId, int page, int pageSize) {
        long total = jpaRepository.countByPostIdAndParentCommentIdIsNullAndIsDeletedFalse(postId);
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostComment> items = jpaRepository
                .findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(postId, pageRequest);

        // Mirror the original's Include(c => c.Replies.Where(r => !r.IsDeleted)):
        // filter out soft-deleted replies from the lazily-loaded collection before returning.
        for (PostComment comment : items) {
            List<PostComment> filteredReplies = comment.getReplies().stream()
                    .filter(r -> !r.isDeleted())
                    .collect(Collectors.toList());
            comment.setReplies(filteredReplies);
        }

        return new PagedResult<>(items, (int) total, page, pageSize);
    }

    @Override
    @Transactional
    public PostComment add(PostComment comment) {
        return jpaRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        jpaRepository.markDeleted(id);
    }
}
