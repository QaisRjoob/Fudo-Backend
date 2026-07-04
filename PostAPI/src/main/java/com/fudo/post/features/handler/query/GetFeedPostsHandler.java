package com.fudo.post.features.handler.query;

import com.fudo.post.application.dto.query.GetFeedPostsQuery;
import com.fudo.post.application.dto.query.GetUserPostsQuery.PostSummaryDto;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetFeedPostsHandler implements RequestHandler<GetFeedPostsQuery, CursorPage<PostSummaryDto>> {

    private final PostRepository posts;

    public GetFeedPostsHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPage<PostSummaryDto> handle(GetFeedPostsQuery query) {
        CursorPage<Post> result = posts.getFeed(query.getAuthorIds(), query.getLimit(), query.getCursor());

        List<PostSummaryDto> items = result.getItems().stream()
                .map(GetUserPostsHandler::toSummary)
                .collect(Collectors.toList());

        return new CursorPage<>(items, result.getNextCursor());
    }
}
