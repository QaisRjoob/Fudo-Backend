package com.fudo.user.features.handler.query;

import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.user.application.dto.query.GetUserProfileQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.FollowRepository;
import com.fudo.user.interfaces.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserProfileHandler implements RequestHandler<GetUserProfileQuery, UserProfileDto> {

    private final UserProfileRepository profiles;
    private final FollowRepository follows;

    public GetUserProfileHandler(UserProfileRepository profiles, FollowRepository follows) {
        this.profiles = profiles;
        this.follows = follows;
    }

    @Override
    public UserProfileDto handle(GetUserProfileQuery query) {
        Optional<UserProfile> profileOpt = query.getUserId() != null
                ? profiles.getById(query.getUserId())
                : profiles.getByUsername(query.getUsername());

        if (profileOpt.isEmpty()) {
            return null;
        }
        UserProfile profile = profileOpt.get();

        boolean isFollowing = !query.getRequestingUserId().equals(profile.getId())
                && follows.isFollowing(query.getRequestingUserId(), profile.getId());

        UserProfileDto dto = new UserProfileDto();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setDisplayName(profile.getDisplayName());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setGender(profile.getGender());
        dto.setFollowerCount(profile.getFollowerCount());
        dto.setFollowingCount(profile.getFollowingCount());
        dto.setPostCount(profile.getPostCount());
        dto.setPrivate(profile.isPrivate());
        dto.setFollowing(isFollowing);
        dto.setOwnProfile(profile.getId().equals(query.getRequestingUserId()));
        dto.setCreatedAt(profile.getCreatedAt());
        return dto;
    }
}
