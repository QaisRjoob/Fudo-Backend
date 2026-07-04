package com.fudo.post.infrastructure.repository;

import com.fudo.post.entity.Post;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.post.repository.PostJpaRepository;
import com.fudo.shared.pagination.CursorHelper;
import com.fudo.shared.pagination.CursorPage;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaPostRepository implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public JpaPostRepository(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Post> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Post> getByIdWithDetails(UUID id) {
        // Collections are mapped with @OrderBy (media/ingredients by sortOrder, steps by stepNumber),
        // so a plain findById already returns them ordered per the original Include(...OrderBy(...)).
        return jpaRepository.findById(id);
    }

    @Override
    public PagedResult<Post> getByAuthor(UUID authorId, int page, int pageSize) {
        long total = jpaRepository.countByAuthorIdAndIsPublishedTrue(authorId);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Post> items = jpaRepository.findByAuthorIdAndIsPublishedTrueOrderByCreatedAtDesc(authorId, pageRequest);
        return new PagedResult<>(items, (int) total, page, pageSize);
    }

    @Override
    public CursorPage<Post> getFeed(List<UUID> authorIds, int limit, String cursor) {
        PageRequest pageRequest = PageRequest.of(0, limit + 1, Sort.unsorted());

        List<Post> items;
        if (cursor != null) {
            CursorHelper.Cursor decoded = CursorHelper.decode(cursor);
            items = jpaRepository.findFeedAfterCursor(authorIds, decoded.createdAt(), decoded.id(), pageRequest);
        } else {
            items = jpaRepository.findFeedFirstPage(authorIds, pageRequest);
        }

        items = new ArrayList<>(items);
        String nextCursor = null;
        if (items.size() > limit) {
            items.remove(items.size() - 1);
            Post last = items.get(items.size() - 1);
            nextCursor = CursorHelper.encode(last.getCreatedAt(), last.getId());
        }

        return new CursorPage<>(items, nextCursor);
    }

    @Override
    public Post add(Post post) {
        return jpaRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        return jpaRepository.save(post);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void incrementLikeCount(UUID postId, int delta) {
        jpaRepository.incrementLikeCount(postId, delta);
    }

    @Override
    @Transactional
    public void incrementCommentCount(UUID postId, int delta) {
        jpaRepository.incrementCommentCount(postId, delta);
    }

    @Override
    @Transactional
    public void incrementSaveCount(UUID postId, int delta) {
        jpaRepository.incrementSaveCount(postId, delta);
    }

    @Override
    @Transactional
    public void incrementAuthorPostCount(UUID authorId, int delta) {
        jpaRepository.incrementAuthorPostCount(authorId, delta);
    }
}
