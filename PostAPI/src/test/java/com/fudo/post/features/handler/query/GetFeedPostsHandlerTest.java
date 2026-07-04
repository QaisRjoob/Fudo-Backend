package com.fudo.post.features.handler.query;

import com.fudo.post.application.dto.query.GetFeedPostsQuery;
import com.fudo.post.application.dto.query.GetUserPostsQuery.PostSummaryDto;
import com.fudo.post.entity.Post;
import com.fudo.post.entity.PostMedia;
import com.fudo.post.entity.PostTag;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.pagination.CursorPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFeedPostsHandlerTest {

    @Mock
    private PostRepository posts;

    private GetFeedPostsHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetFeedPostsHandler(posts);
    }

    @Test
    void handle_mapsRepositoryCursorPageToSummaryDtos_andPropagatesNextCursor() {
        UUID authorId = UUID.randomUUID();
        List<UUID> authorIds = List.of(authorId);

        Post post = new Post();
        post.setAuthorId(authorId);
        post.setTitle("Pasta");
        post.setLikeCount(3);
        post.setCommentCount(1);
        post.setCreatedAt(Instant.parse("2026-01-01T00:00:00Z"));

        PostMedia media = new PostMedia();
        media.setUrl("thumb.jpg");
        media.setSortOrder(0);
        post.setMedia(List.of(media));

        PostTag tag = new PostTag();
        tag.setTag("italian");
        post.setTags(List.of(tag));

        String expectedCursor = "next-cursor-token";
        when(posts.getFeed(authorIds, 20, null)).thenReturn(new CursorPage<>(List.of(post), expectedCursor));

        GetFeedPostsQuery query = new GetFeedPostsQuery();
        query.setAuthorIds(authorIds);
        query.setLimit(20);
        query.setCursor(null);

        CursorPage<PostSummaryDto> result = handler.handle(query);

        assertThat(result.getNextCursor()).isEqualTo(expectedCursor);
        assertThat(result.getItems()).hasSize(1);
        PostSummaryDto dto = result.getItems().get(0);
        assertThat(dto.getTitle()).isEqualTo("Pasta");
        assertThat(dto.getThumbnailUrl()).isEqualTo("thumb.jpg");
        assertThat(dto.getTags()).containsExactly("italian");
        assertThat(dto.getLikeCount()).isEqualTo(3);
    }

    @Test
    void handle_passesCursorThrough_toRepository() {
        UUID authorId = UUID.randomUUID();
        List<UUID> authorIds = List.of(authorId);
        when(posts.getFeed(authorIds, 10, "abc")).thenReturn(new CursorPage<>(List.of(), null));

        GetFeedPostsQuery query = new GetFeedPostsQuery();
        query.setAuthorIds(authorIds);
        query.setLimit(10);
        query.setCursor("abc");

        CursorPage<PostSummaryDto> result = handler.handle(query);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getNextCursor()).isNull();
    }
}
