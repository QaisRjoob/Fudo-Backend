package com.fudo.post.interfaces;

import com.fudo.post.entity.Post;
import com.fudo.shared.pagination.CursorPage;
import com.fudo.shared.pagination.PagedResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Synchronous equivalent of PostAPI's {@code IPostRepository} from the .NET source.
 */
public interface PostRepository {

    Optional<Post> getById(UUID id);

    Optional<Post> getByIdWithDetails(UUID id);

    PagedResult<Post> getByAuthor(UUID authorId, int page, int pageSize);

    CursorPage<Post> getFeed(List<UUID> authorIds, int limit, String cursor);

    Post add(Post post);

    Post update(Post post);

    void delete(UUID id);

    void incrementLikeCount(UUID postId, int delta);

    void incrementCommentCount(UUID postId, int delta);

    void incrementSaveCount(UUID postId, int delta);

    void incrementAuthorPostCount(UUID authorId, int delta);
}
