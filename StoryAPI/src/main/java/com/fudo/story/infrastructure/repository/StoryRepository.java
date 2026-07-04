package com.fudo.story.infrastructure.repository;

import com.fudo.story.entity.Story;
import com.fudo.story.entity.StoryReply;
import com.fudo.story.entity.StoryView;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.story.repository.StoryJpaRepository;
import com.fudo.story.repository.StoryReplyJpaRepository;
import com.fudo.story.repository.StoryViewJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StoryRepository implements IStoryRepository {

    private final StoryJpaRepository stories;
    private final StoryViewJpaRepository views;
    private final StoryReplyJpaRepository replies;

    public StoryRepository(StoryJpaRepository stories, StoryViewJpaRepository views, StoryReplyJpaRepository replies) {
        this.stories = stories;
        this.views = views;
        this.replies = replies;
    }

    @Override
    public Optional<Story> getById(UUID id) {
        return stories.findByIdAndIsDeletedFalse(id);
    }

    @Override
    public List<Story> getActiveByAuthor(UUID authorId) {
        return stories.findByAuthorIdAndIsDeletedFalseAndExpiresAtAfterOrderByCreatedAtDesc(authorId, Instant.now());
    }

    @Override
    public List<Story> getFeedStories(List<UUID> authorIds) {
        return stories.findFeedStories(authorIds, Instant.now());
    }

    @Override
    public List<Story> getArchiveByAuthor(UUID authorId, int page, int pageSize) {
        // Original: .Skip((page - 1) * pageSize).Take(pageSize), 1-based page index.
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize);
        return stories.findArchiveByAuthor(authorId, Instant.now(), pageRequest);
    }

    @Override
    @Transactional
    public void add(Story story) {
        stories.save(story);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        stories.markDeleted(id);
    }

    @Override
    @Transactional
    public void recordView(UUID storyId, UUID viewerId) {
        boolean alreadyViewed = views.existsByStoryIdAndViewerId(storyId, viewerId);
        if (alreadyViewed) {
            return;
        }

        StoryView view = new StoryView();
        view.setStoryId(storyId);
        view.setViewerId(viewerId);
        views.save(view);
        stories.incrementViewCount(storyId);
    }

    @Override
    public boolean hasViewed(UUID storyId, UUID viewerId) {
        return views.existsByStoryIdAndViewerId(storyId, viewerId);
    }

    @Override
    @Transactional
    public void addReply(StoryReply reply) {
        replies.save(reply);
    }

    @Override
    @Transactional
    public void archiveExpired() {
        stories.archiveExpired(Instant.now());
    }
}
