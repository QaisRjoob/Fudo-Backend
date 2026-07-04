package com.fudo.feed.interfaces;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.shared.pagination.CursorPage;

import java.util.List;
import java.util.UUID;

/** Mirrors the original .NET FeedAPI's {@code IFeedRepository}. */
public interface IFeedRepository {

    List<UUID> getFollowingIds(UUID userId);

    CursorPage<FeedPost> getFeed(List<UUID> authorIds, UUID requestingUserId, int limit, String cursor);

    CursorPage<FeedPost> getExploreFeed(UUID userId, int limit, String cursor);
}
