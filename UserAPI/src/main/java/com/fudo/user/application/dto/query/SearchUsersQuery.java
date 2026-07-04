package com.fudo.user.application.dto.query;

import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.PagedResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUsersQuery implements Request<PagedResult<GetUserProfileQuery.UserProfileDto>> {

    private String query;
    private int page = 1;
    private int pageSize = 20;
}
