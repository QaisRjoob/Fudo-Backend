package com.fudo.interaction.interfaces;

import com.fudo.interaction.entity.PostComment;
import com.fudo.shared.pagination.PagedResult;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {

    Optional<PostComment> getById(UUID id);

    PagedResult<PostComment> getByPost(UUID postId, int page, int pageSize);

    PostComment add(PostComment comment);

    void delete(UUID id);
}
