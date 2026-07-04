package com.fudo.feed.features.handler.query;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.application.dto.query.GetExploreFeedQuery;
import com.fudo.feed.interfaces.IFeedRepository;
import com.fudo.shared.pagination.CursorPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetExploreFeedHandlerTest {

    @Mock
    private IFeedRepository feedRepository;

    @Test
    void handle_delegatesDirectlyToGetExploreFeed_withoutLookingUpFollowingIdsItself() {
        UUID userId = UUID.randomUUID();
        CursorPage<FeedPost> expected = new CursorPage<>(List.of(new FeedPost()), "cursor-2");
        when(feedRepository.getExploreFeed(userId, 20, null)).thenReturn(expected);

        GetExploreFeedHandler handler = new GetExploreFeedHandler(feedRepository);
        CursorPage<FeedPost> result = handler.handle(new GetExploreFeedQuery(userId, 20, null));

        assertThat(result).isSameAs(expected);
        verify(feedRepository).getExploreFeed(userId, 20, null);
    }

    @Test
    void handle_passesCursorAndLimitThrough() {
        UUID userId = UUID.randomUUID();
        when(feedRepository.getExploreFeed(userId, 10, "xyz"))
                .thenReturn(new CursorPage<>(List.of(), null));

        GetExploreFeedHandler handler = new GetExploreFeedHandler(feedRepository);
        CursorPage<FeedPost> result = handler.handle(new GetExploreFeedQuery(userId, 10, "xyz"));

        assertThat(result.isHasMore()).isFalse();
        verify(feedRepository).getExploreFeed(userId, 10, "xyz");
    }
}
