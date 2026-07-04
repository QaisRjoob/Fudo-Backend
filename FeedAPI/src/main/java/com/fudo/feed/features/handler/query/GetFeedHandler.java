package com.fudo.feed.features.handler.query;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.application.dto.query.GetFeedQuery;
import com.fudo.feed.interfaces.IFeedRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetFeedHandler implements RequestHandler<GetFeedQuery, CursorPage<FeedPost>> {

    private final IFeedRepository feed;

    public GetFeedHandler(IFeedRepository feed) {
        this.feed = feed;
    }

    @Override
    public CursorPage<FeedPost> handle(GetFeedQuery query) {
        List<UUID> followingIds = feed.getFollowingIds(query.getUserId());
        return feed.getFeed(followingIds, query.getUserId(), query.getLimit(), query.getCursor());
    }
}
