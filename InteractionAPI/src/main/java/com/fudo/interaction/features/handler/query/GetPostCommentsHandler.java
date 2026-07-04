package com.fudo.interaction.features.handler.query;

import com.fudo.interaction.application.dto.query.CommentDto;
import com.fudo.interaction.application.dto.query.GetPostCommentsQuery;
import com.fudo.interaction.entity.PostComment;
import com.fudo.interaction.interfaces.CommentRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetPostCommentsHandler implements RequestHandler<GetPostCommentsQuery, PagedResult<CommentDto>> {

    private final CommentRepository comments;

    public GetPostCommentsHandler(CommentRepository comments) {
        this.comments = comments;
    }

    @Override
    public PagedResult<CommentDto> handle(GetPostCommentsQuery query) {
        PagedResult<PostComment> result = comments.getByPost(query.getPostId(), query.getPage(), query.getPageSize());
        List<CommentDto> items = result.getItems().stream().map(GetPostCommentsHandler::mapComment).collect(Collectors.toList());
        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }

    private static CommentDto mapComment(PostComment c) {
        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setPostId(c.getPostId());
        dto.setAuthorId(c.getAuthorId());
        dto.setParentCommentId(c.getParentCommentId());
        dto.setContent(c.getContent());
        dto.setLikeCount(c.getLikeCount());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setReplies(c.getReplies().stream().map(GetPostCommentsHandler::mapComment).collect(Collectors.toList()));
        return dto;
    }
}
