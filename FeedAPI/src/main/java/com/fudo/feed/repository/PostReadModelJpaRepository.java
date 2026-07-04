package com.fudo.feed.repository;

import com.fudo.feed.entity.PostReadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PostReadModelJpaRepository extends JpaRepository<PostReadModel, UUID> {

    /**
     * Following feed page (no cursor): authored by one of authorIds, or by the
     * requesting user themselves; published only; newest first, id DESC as a
     * tie-break. Mirrors FeedRepository.GetFeedAsync's no-cursor branch.
     */
    @Query(value = "select * from posts p " +
            "where (p.author_id in (:authorIds) or p.author_id = :requestingUserId) " +
            "and p.is_published = 1 " +
            "order by p.created_at desc, p.id desc " +
            "offset 0 rows fetch next :pageSize rows only",
            nativeQuery = true)
    List<PostReadModel> findFeedFirstPage(@Param("authorIds") List<UUID> authorIds,
                                           @Param("requestingUserId") UUID requestingUserId,
                                           @Param("pageSize") int pageSize);

    /**
     * Following feed page (with cursor): same predicate, plus the cursor
     * filter (createdAt &lt; cursorDate OR (createdAt == cursorDate AND id &lt;
     * cursorId)), exactly as FeedRepository.GetFeedAsync applies it.
     */
    @Query(value = "select * from posts p " +
            "where (p.author_id in (:authorIds) or p.author_id = :requestingUserId) " +
            "and p.is_published = 1 " +
            "and (p.created_at < :cursorDate or (p.created_at = :cursorDate and p.id < :cursorId)) " +
            "order by p.created_at desc, p.id desc " +
            "offset 0 rows fetch next :pageSize rows only",
            nativeQuery = true)
    List<PostReadModel> findFeedPageAfterCursor(@Param("authorIds") List<UUID> authorIds,
                                                 @Param("requestingUserId") UUID requestingUserId,
                                                 @Param("cursorDate") Instant cursorDate,
                                                 @Param("cursorId") UUID cursorId,
                                                 @Param("pageSize") int pageSize);

    /**
     * Explore feed page (no cursor): authored by someone NOT in excludedAuthorIds
     * (followingIds + self); published only; ordered by like_count DESC, then
     * created_at DESC - note no id tie-break, exactly matching
     * FeedRepository.GetExploreFeedAsync's `.OrderByDescending(LikeCount)
     * .ThenByDescending(CreatedAt)`.
     */
    @Query(value = "select * from posts p " +
            "where p.author_id not in (:excludedAuthorIds) " +
            "and p.is_published = 1 " +
            "order by p.like_count desc, p.created_at desc " +
            "offset 0 rows fetch next :pageSize rows only",
            nativeQuery = true)
    List<PostReadModel> findExploreFeedFirstPage(@Param("excludedAuthorIds") List<UUID> excludedAuthorIds,
                                                  @Param("pageSize") int pageSize);

    /**
     * Explore feed page (with cursor): same predicate and ordering, plus the
     * SAME cursor filter shape used by the following feed (createdAt /
     * id-based). This is a faithful port of a quirk in the original .NET
     * source: GetExploreFeedAsync sorts by (likeCount DESC, createdAt DESC)
     * but filters the cursor against (createdAt, id) - i.e. the cursor
     * predicate does not match the feed's own sort order. Not "fixed" here;
     * ported exactly as written.
     */
    @Query(value = "select * from posts p " +
            "where p.author_id not in (:excludedAuthorIds) " +
            "and p.is_published = 1 " +
            "and (p.created_at < :cursorDate or (p.created_at = :cursorDate and p.id < :cursorId)) " +
            "order by p.like_count desc, p.created_at desc " +
            "offset 0 rows fetch next :pageSize rows only",
            nativeQuery = true)
    List<PostReadModel> findExploreFeedPageAfterCursor(@Param("excludedAuthorIds") List<UUID> excludedAuthorIds,
                                                        @Param("cursorDate") Instant cursorDate,
                                                        @Param("cursorId") UUID cursorId,
                                                        @Param("pageSize") int pageSize);
}
