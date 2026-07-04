package com.fudo.feed.features.handler.query;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.application.dto.query.GetExploreFeedQuery;
import com.fudo.feed.interfaces.IFeedRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.stereotype.Component;

@Component
public class GetExploreFeedHandler implements RequestHandler<GetExploreFeedQuery, CursorPage<FeedPost>> {

    private final IFeedRepository feed;

    public GetExploreFeedHandler(IFeedRepository feed) {
        this.feed = feed;
    }

    @Override
    public CursorPage<FeedPost> handle(GetExploreFeedQuery query) {
        return feed.getExploreFeed(query.getUserId(), query.getLimit(), query.getCursor());
    }
}
