package com.fudo.feed.application.dto.query;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.CursorPage;

import java.util.UUID;

/** Explore/discovery feed: posts from accounts the requesting user does NOT follow (and isn't themselves). */
public class GetExploreFeedQuery implements Request<CursorPage<FeedPost>> {

    private UUID userId;
    private int limit = 20;
    private String cursor;

    public GetExploreFeedQuery() {
    }

    public GetExploreFeedQuery(UUID userId, int limit, String cursor) {
        this.userId = userId;
        this.limit = limit;
        this.cursor = cursor;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
