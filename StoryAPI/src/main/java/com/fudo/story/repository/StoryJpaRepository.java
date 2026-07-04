package com.fudo.story.repository;

import com.fudo.story.entity.Story;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoryJpaRepository extends JpaRepository<Story, UUID> {

    @EntityGraph(attributePaths = "views")
    Optional<Story> findByIdAndIsDeletedFalse(UUID id);

    List<Story> findByAuthorIdAndIsDeletedFalseAndExpiresAtAfterOrderByCreatedAtDesc(UUID authorId, Instant now);

    @EntityGraph(attributePaths = "views")
    @Query("select s from Story s where s.authorId in :authorIds and s.isDeleted = false and s.expiresAt > :now order by s.createdAt desc")
    List<Story> findFeedStories(@Param("authorIds") List<UUID> authorIds, @Param("now") Instant now);

    @Query("select s from Story s where s.authorId = :authorId and s.isDeleted = false " +
            "and (s.isArchived = true or s.expiresAt <= :now) order by s.createdAt desc")
    List<Story> findArchiveByAuthor(@Param("authorId") UUID authorId, @Param("now") Instant now,
                                     org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("update Story s set s.isDeleted = true where s.id = :id")
    void markDeleted(@Param("id") UUID id);

    @Modifying
    @Query("update Story s set s.viewCount = s.viewCount + 1 where s.id = :id")
    void incrementViewCount(@Param("id") UUID id);

    @Modifying
    @Query("update Story s set s.isArchived = true where s.isDeleted = false and s.isArchived = false and s.expiresAt <= :now")
    void archiveExpired(@Param("now") Instant now);
}
