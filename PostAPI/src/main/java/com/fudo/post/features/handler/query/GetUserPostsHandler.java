package com.fudo.post.features.handler.query;

import com.fudo.post.application.dto.query.GetUserPostsQuery;
import com.fudo.post.application.dto.query.GetUserPostsQuery.PostSummaryDto;
import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetUserPostsHandler implements RequestHandler<GetUserPostsQuery, PagedResult<PostSummaryDto>> {

    private final PostRepository posts;

    public GetUserPostsHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<PostSummaryDto> handle(GetUserPostsQuery query) {
        PagedResult<Post> result = posts.getByAuthor(query.getAuthorId(), query.getPage(), query.getPageSize());

        List<PostSummaryDto> items = result.getItems().stream()
                .map(GetUserPostsHandler::toSummary)
                .collect(Collectors.toList());

        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }

    static PostSummaryDto toSummary(Post p) {
        PostSummaryDto dto = new PostSummaryDto();
        dto.setId(p.getId());
        dto.setAuthorId(p.getAuthorId());
        dto.setTitle(p.getTitle());
        dto.setThumbnailUrl(p.getMedia().isEmpty() ? null : p.getMedia().get(0).getUrl());
        dto.setLikeCount(p.getLikeCount());
        dto.setCommentCount(p.getCommentCount());
        dto.setTags(p.getTags().stream().map(t -> t.getTag()).collect(Collectors.toList()));
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
