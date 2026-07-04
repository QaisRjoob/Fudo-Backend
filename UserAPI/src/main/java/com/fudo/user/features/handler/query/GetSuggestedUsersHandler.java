package com.fudo.user.features.handler.query;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.user.application.dto.query.GetSuggestedUsersQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetSuggestedUsersHandler implements RequestHandler<GetSuggestedUsersQuery, List<UserProfileDto>> {

    private final UserProfileRepository profiles;

    public GetSuggestedUsersHandler(UserProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Override
    public List<UserProfileDto> handle(GetSuggestedUsersQuery query) {
        List<UserProfile> suggested = profiles.getSuggested(query.getUserId(), query.getLimit());

        return suggested.stream().map(u -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setDisplayName(u.getDisplayName());
            dto.setAvatarUrl(u.getAvatarUrl());
            dto.setFollowerCount(u.getFollowerCount());
            dto.setBio(u.getBio());
            return dto;
        }).toList();
    }
}
