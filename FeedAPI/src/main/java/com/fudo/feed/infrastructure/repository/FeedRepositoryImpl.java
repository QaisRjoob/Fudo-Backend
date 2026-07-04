package com.fudo.feed.infrastructure.repository;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.entity.PostMediaReadModel;
import com.fudo.feed.entity.PostReadModel;
import com.fudo.feed.entity.PostTagReadModel;
import com.fudo.feed.interfaces.IFeedRepository;
import com.fudo.feed.repository.FollowReadModelJpaRepository;
import com.fudo.feed.repository.PostMediaReadModelJpaRepository;
import com.fudo.feed.repository.PostReadModelJpaRepository;
import com.fudo.feed.repository.PostTagReadModelJpaRepository;
import com.fudo.shared.pagination.CursorHelper;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mirrors the original .NET FeedAPI's {@code FeedRepository}: FeedAPI owns no
 * tables of its own, it reads (read-only) directly from the same physical
 * "posts" / "follows" / "post_tags" / "post_media" tables that PostAPI and
 * UserAPI own, in the shared FudoDB database.
 */
@Repository
public class FeedRepositoryImpl implements IFeedRepository {

    /** A GUID that can never match a real author id, used to keep "IN (...)" valid SQL when a list is empty. */
    private static final UUID NONE_MARKER = new UUID(0L, 0L);

    private final PostReadModelJpaRepository posts;
    private final FollowReadModelJpaRepository follows;
    private final PostTagReadModelJpaRepository postTags;
    private final PostMediaReadModelJpaRepository postMedia;

    public FeedRepositoryImpl(PostReadModelJpaRepository posts,
                               FollowReadModelJpaRepository follows,
                               PostTagReadModelJpaRepository postTags,
                               PostMediaReadModelJpaRepository postMedia) {
        this.posts = posts;
        this.follows = follows;
        this.postTags = postTags;
        this.postMedia = postMedia;
    }

    @Override
    public List<UUID> getFollowingIds(UUID userId) {
        return follows.findFolloweeIdsByFollowerId(userId);
    }

    @Override
    public CursorPage<FeedPost> getFeed(List<UUID> authorIds, UUID requestingUserId, int limit, String cursor) {
        List<UUID> safeAuthorIds = nonEmpty(authorIds);

        List<PostReadModel> page;
        if (cursor != null) {
            CursorHelper.Cursor decoded = CursorHelper.decode(cursor);
            page = posts.findFeedPageAfterCursor(safeAuthorIds, requestingUserId,
                    decoded.createdAt(), decoded.id(), limit + 1);
        } else {
            page = posts.findFeedFirstPage(safeAuthorIds, requestingUserId, limit + 1);
        }

        return assemble(page, limit);
    }

    @Override
    public CursorPage<FeedPost> getExploreFeed(UUID userId, int limit, String cursor) {
        List<UUID> followingIds = new ArrayList<>(getFollowingIds(userId));
        followingIds.add(userId);
        List<UUID> excludedAuthorIds = nonEmpty(followingIds);

        List<PostReadModel> page;
        if (cursor != null) {
            CursorHelper.Cursor decoded = CursorHelper.decode(cursor);
            page = posts.findExploreFeedPageAfterCursor(excludedAuthorIds,
                    decoded.createdAt(), decoded.id(), limit + 1);
        } else {
            page = posts.findExploreFeedFirstPage(excludedAuthorIds, limit + 1);
        }

        return assemble(page, limit);
    }

    private CursorPage<FeedPost> assemble(List<PostReadModel> page, int limit) {
        List<PostReadModel> pagePosts = new ArrayList<>(page);

        String nextCursor = null;
        if (pagePosts.size() > limit) {
            pagePosts.remove(pagePosts.size() - 1);
            PostReadModel last = pagePosts.get(pagePosts.size() - 1);
            nextCursor = CursorHelper.encode(last.getCreatedAt(), last.getId());
        }

        List<UUID> postIds = pagePosts.stream().map(PostReadModel::getId).collect(Collectors.toList());

        Map<UUID, List<String>> tagsByPostId = new HashMap<>();
        Map<UUID, String> thumbnailByPostId = new HashMap<>();
        if (!postIds.isEmpty()) {
            for (PostTagReadModel tag : postTags.findByPostIdIn(postIds)) {
                tagsByPostId.computeIfAbsent(tag.getPostId(), k -> new ArrayList<>()).add(tag.getTag());
            }
            for (PostMediaReadModel media : postMedia.findByPostIdInAndSortOrder(postIds, 0)) {
                thumbnailByPostId.put(media.getPostId(), media.getUrl());
            }
        }

        List<FeedPost> items = new ArrayList<>(pagePosts.size());
        for (PostReadModel p : pagePosts) {
            FeedPost item = new FeedPost();
            item.setId(p.getId());
            item.setAuthorId(p.getAuthorId());
            item.setTitle(p.getTitle());
            item.setThumbnailUrl(thumbnailByPostId.get(p.getId()));
            item.setLikeCount(p.getLikeCount());
            item.setCommentCount(p.getCommentCount());
            item.setTags(tagsByPostId.getOrDefault(p.getId(), Collections.emptyList()));
            item.setCreatedAt(p.getCreatedAt());
            items.add(item);
        }

        return new CursorPage<>(items, nextCursor);
    }

    private List<UUID> nonEmpty(List<UUID> ids) {
        return (ids == null || ids.isEmpty()) ? List.of(NONE_MARKER) : ids;
    }
}
