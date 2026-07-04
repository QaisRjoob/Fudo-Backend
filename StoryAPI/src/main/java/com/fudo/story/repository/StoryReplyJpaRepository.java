package com.fudo.story.repository;

import com.fudo.story.entity.StoryReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoryReplyJpaRepository extends JpaRepository<StoryReply, UUID> {
}
