package com.fudo.story.interfaces;

import com.fudo.story.entity.Story;
import com.fudo.story.entity.StoryReply;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStoryRepository {

    Optional<Story> getById(UUID id);

    List<Story> getActiveByAuthor(UUID authorId);

    List<Story> getFeedStories(List<UUID> authorIds);

    List<Story> getArchiveByAuthor(UUID authorId, int page, int pageSize);

    void add(Story story);

    void delete(UUID id);

    void recordView(UUID storyId, UUID viewerId);

    boolean hasViewed(UUID storyId, UUID viewerId);

    void addReply(StoryReply reply);

    void archiveExpired();
}
