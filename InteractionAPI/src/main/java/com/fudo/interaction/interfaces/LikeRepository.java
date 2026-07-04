package com.fudo.interaction.interfaces;

import java.util.List;
import java.util.UUID;

public interface LikeRepository {

    boolean hasLiked(UUID postId, UUID userId);

    void like(UUID postId, UUID userId);

    void unlike(UUID postId, UUID userId);

    int getLikeCount(UUID postId);

    List<UUID> getLikerIds(UUID postId, int page, int pageSize);
}
