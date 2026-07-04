package com.fudo.user.interfaces;

import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.entity.UserProfile;

import java.util.List;
import java.util.UUID;

public interface FollowRepository {

    boolean isFollowing(UUID followerId, UUID followeeId);

    void follow(UUID followerId, UUID followeeId);

    void unfollow(UUID followerId, UUID followeeId);

    PagedResult<UserProfile> getFollowers(UUID userId, int page, int pageSize);

    PagedResult<UserProfile> getFollowing(UUID userId, int page, int pageSize);

    List<UUID> getFollowingIds(UUID userId);
}
