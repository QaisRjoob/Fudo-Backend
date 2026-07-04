package com.fudo.post.repository;

import com.fudo.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PostJpaRepository extends JpaRepository<Post, UUID> {

    long countByAuthorIdAndIsPublishedTrue(UUID authorId);

    List<Post> findByAuthorIdAndIsPublishedTrueOrderByCreatedAtDesc(UUID authorId, org.springframework.data.domain.Pageable pageable);

    @Query("select p from Post p where p.authorId in :authorIds and p.isPublished = true "
            + "order by p.createdAt desc, p.id desc")
    List<Post> findFeedFirstPage(@Param("authorIds") List<UUID> authorIds, org.springframework.data.domain.Pageable pageable);

    @Query("select p from Post p where p.authorId in :authorIds and p.isPublished = true "
            + "and (p.createdAt < :cursorDate or (p.createdAt = :cursorDate and p.id < :cursorId)) "
            + "order by p.createdAt desc, p.id desc")
    List<Post> findFeedAfterCursor(@Param("authorIds") List<UUID> authorIds,
                                    @Param("cursorDate") Instant cursorDate,
                                    @Param("cursorId") UUID cursorId,
                                    org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + :delta where p.id = :postId")
    void incrementLikeCount(@Param("postId") UUID postId, @Param("delta") int delta);

    @Modifying
    @Query("update Post p set p.commentCount = p.commentCount + :delta where p.id = :postId")
    void incrementCommentCount(@Param("postId") UUID postId, @Param("delta") int delta);

    @Modifying
    @Query("update Post p set p.saveCount = p.saveCount + :delta where p.id = :postId")
    void incrementSaveCount(@Param("postId") UUID postId, @Param("delta") int delta);

    /**
     * Cross-service write into UserAPI's table. PostAPI and UserAPI share the same physical
     * "FudoDB" database (a tight-coupling pattern inherited unchanged from the original .NET
     * source, which ran raw SQL "UPDATE UserProfiles SET PostCount = PostCount + {0} WHERE Id = {1}"
     * directly against the shared database). UserAPI maps UserProfile to table "user_profiles"
     * with snake_case columns "post_count" / "id" via Hibernate's default naming strategy.
     */
    @Modifying
    @Query(value = "UPDATE user_profiles SET post_count = post_count + :delta WHERE id = :authorId", nativeQuery = true)
    void incrementAuthorPostCount(@Param("authorId") UUID authorId, @Param("delta") int delta);
}
