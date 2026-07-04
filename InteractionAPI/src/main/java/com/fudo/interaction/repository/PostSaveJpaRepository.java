package com.fudo.interaction.repository;

import com.fudo.interaction.entity.PostSave;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostSaveJpaRepository extends JpaRepository<PostSave, UUID> {

    boolean existsByPostIdAndUserId(UUID postId, UUID userId);

    long countByUserId(UUID userId);

    List<PostSave> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Modifying
    @Query("delete from PostSave s where s.postId = :postId and s.userId = :userId")
    void deleteByPostIdAndUserId(@Param("postId") UUID postId, @Param("userId") UUID userId);
}
