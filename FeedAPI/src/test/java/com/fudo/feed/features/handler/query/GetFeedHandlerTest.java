package com.fudo.feed.features.handler.query;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.application.dto.query.GetFeedQuery;
import com.fudo.feed.interfaces.IFeedRepository;
import com.fudo.shared.pagination.CursorPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFeedHandlerTest {

    @Mock
    private IFeedRepository feedRepository;

    @Test
    void handle_looksUpFollowingIdsThenDelegatesToGetFeed() {
        UUID userId = UUID.randomUUID();
        List<UUID> followingIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        CursorPage<FeedPost> expected = new CursorPage<>(List.of(new FeedPost()), null);

        when(feedRepository.getFollowingIds(userId)).thenReturn(followingIds);
        when(feedRepository.getFeed(eq(followingIds), eq(userId), anyInt(), any()))
                .thenReturn(expected);

        GetFeedHandler handler = new GetFeedHandler(feedRepository);
        CursorPage<FeedPost> result = handler.handle(new GetFeedQuery(userId, 20, null));

        assertThat(result).isSameAs(expected);
        verify(feedRepository).getFollowingIds(userId);
        verify(feedRepository).getFeed(followingIds, userId, 20, null);
    }

    @Test
    void handle_passesCursorAndLimitThrough() {
        UUID userId = UUID.randomUUID();
        when(feedRepository.getFollowingIds(userId)).thenReturn(List.of());
        when(feedRepository.getFeed(any(), eq(userId), eq(5), eq("abc")))
                .thenReturn(new CursorPage<>(List.of(), "next-cursor"));

        GetFeedHandler handler = new GetFeedHandler(feedRepository);
        CursorPage<FeedPost> result = handler.handle(new GetFeedQuery(userId, 5, "abc"));

        assertThat(result.getNextCursor()).isEqualTo("next-cursor");
        verify(feedRepository).getFeed(List.of(), userId, 5, "abc");
    }
}
