package com.fudo.post.application.dto.query;

import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.PagedResult;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetUserPostsQuery implements Request<PagedResult<GetUserPostsQuery.PostSummaryDto>> {

    private UUID authorId;
    private int page = 1;
    private int pageSize = 20;

    @Getter
    @Setter
    public static class PostSummaryDto {
        private UUID id;
        private UUID authorId;
        private String title;
        private String thumbnailUrl;
        private int likeCount;
        private int commentCount;
        private List<String> tags = new ArrayList<>();
        private Instant createdAt;
    }
}
