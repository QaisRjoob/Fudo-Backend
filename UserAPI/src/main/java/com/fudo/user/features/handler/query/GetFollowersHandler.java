package com.fudo.user.features.handler.query;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.application.dto.query.GetFollowersQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.FollowRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetFollowersHandler implements RequestHandler<GetFollowersQuery, PagedResult<UserProfileDto>> {

    private final FollowRepository follows;

    public GetFollowersHandler(FollowRepository follows) {
        this.follows = follows;
    }

    @Override
    public PagedResult<UserProfileDto> handle(GetFollowersQuery query) {
        PagedResult<UserProfile> result = follows.getFollowers(query.getUserId(), query.getPage(), query.getPageSize());

        List<UserProfileDto> items = result.getItems().stream().map(u -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setDisplayName(u.getDisplayName());
            dto.setAvatarUrl(u.getAvatarUrl());
            dto.setFollowerCount(u.getFollowerCount());
            return dto;
        }).toList();

        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }
}
