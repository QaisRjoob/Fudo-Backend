package com.fudo.interaction.application.dto.query;

import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.PagedResult;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetPostCommentsQuery implements Request<PagedResult<CommentDto>> {
    private UUID postId;
    private int page = 1;
    private int pageSize = 20;
}
