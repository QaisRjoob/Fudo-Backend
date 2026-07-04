package com.fudo.user.interfaces;

import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {

    Optional<UserProfile> getById(UUID id);

    Optional<UserProfile> getByUsername(String username);

    void add(UserProfile profile);

    void update(UserProfile profile);

    PagedResult<UserProfile> search(String query, int page, int pageSize);

    List<UserProfile> getSuggested(UUID userId, int limit);
}
