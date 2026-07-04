package com.fudo.interaction.repository;

import com.fudo.interaction.entity.PostComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostCommentJpaRepository extends JpaRepository<PostComment, UUID> {

    Optional<PostComment> findByIdAndIsDeletedFalse(UUID id);

    long countByPostIdAndParentCommentIdIsNullAndIsDeletedFalse(UUID postId);

    List<PostComment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(
            UUID postId, Pageable pageable);

    @Modifying
    @Query("update PostComment c set c.isDeleted = true where c.id = :id")
    void markDeleted(@Param("id") UUID id);
}
