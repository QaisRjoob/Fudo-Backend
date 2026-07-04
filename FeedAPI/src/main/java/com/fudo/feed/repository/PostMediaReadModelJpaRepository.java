package com.fudo.feed.repository;

import com.fudo.feed.entity.PostMediaReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostMediaReadModelJpaRepository extends JpaRepository<PostMediaReadModel, UUID> {

    List<PostMediaReadModel> findByPostIdInAndSortOrder(List<UUID> postIds, int sortOrder);
}
