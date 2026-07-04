package com.fudo.feed.repository;

import com.fudo.feed.entity.FollowReadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FollowReadModelJpaRepository extends JpaRepository<FollowReadModel, UUID> {

    @Query("select f.followeeId from FollowReadModel f where f.followerId = :followerId")
    List<UUID> findFolloweeIdsByFollowerId(UUID followerId);
}
