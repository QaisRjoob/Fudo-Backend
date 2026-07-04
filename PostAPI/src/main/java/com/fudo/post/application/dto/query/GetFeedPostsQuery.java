package com.fudo.post.application.dto.query;

import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.CursorPage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetFeedPostsQuery implements Request<CursorPage<GetUserPostsQuery.PostSummaryDto>> {

    private List<UUID> authorIds = new ArrayList<>();
    private int limit = 20;
    private String cursor;
}
