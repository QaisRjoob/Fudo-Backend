package com.fudo.story.repository;

import com.fudo.story.entity.StoryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoryViewJpaRepository extends JpaRepository<StoryView, UUID> {

    boolean existsByStoryIdAndViewerId(UUID storyId, UUID viewerId);
}
