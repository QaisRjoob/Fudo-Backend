package com.fudo.feed.repository;

import com.fudo.feed.entity.PostTagReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostTagReadModelJpaRepository extends JpaRepository<PostTagReadModel, UUID> {

    List<PostTagReadModel> findByPostIdIn(List<UUID> postIds);
}
