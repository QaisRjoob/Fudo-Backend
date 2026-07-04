package com.fudo.user.features.handler.query;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.application.dto.query.SearchUsersQuery;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchUsersHandler implements RequestHandler<SearchUsersQuery, PagedResult<UserProfileDto>> {

    private final UserProfileRepository profiles;

    public SearchUsersHandler(UserProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Override
    public PagedResult<UserProfileDto> handle(SearchUsersQuery query) {
        PagedResult<UserProfile> result = profiles.search(query.getQuery(), query.getPage(), query.getPageSize());

        List<UserProfileDto> items = result.getItems().stream().map(u -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setDisplayName(u.getDisplayName());
            dto.setAvatarUrl(u.getAvatarUrl());
            dto.setFollowerCount(u.getFollowerCount());
            dto.setBio(u.getBio());
            return dto;
        }).toList();

        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }
}
