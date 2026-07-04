package com.fudo.interaction.interfaces;

import com.fudo.shared.pagination.PagedResult;

import java.util.UUID;

public interface SaveRepository {

    boolean hasSaved(UUID postId, UUID userId);

    void save(UUID postId, UUID userId);

    void unsave(UUID postId, UUID userId);

    PagedResult<UUID> getSavedPostIds(UUID userId, int page, int pageSize);
}
