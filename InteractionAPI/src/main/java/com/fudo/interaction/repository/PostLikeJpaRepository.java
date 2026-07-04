package com.fudo.interaction.repository;

import com.fudo.interaction.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, UUID> {

    boolean existsByPostIdAndUserId(UUID postId, UUID userId);

    long countByPostId(UUID postId);

    List<PostLike> findByPostIdOrderByCreatedAtDesc(UUID postId, org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("delete from PostLike l where l.postId = :postId and l.userId = :userId")
    void deleteByPostIdAndUserId(@Param("postId") UUID postId, @Param("userId") UUID userId);
}
