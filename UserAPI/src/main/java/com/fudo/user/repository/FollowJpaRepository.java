package com.fudo.user.repository;

import com.fudo.user.entity.Follow;
import com.fudo.user.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FollowJpaRepository extends JpaRepository<Follow, UUID> {

    boolean existsByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);

    long deleteByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);

    @Query("select f.followeeId from Follow f where f.followerId = :userId")
    List<UUID> findFolloweeIdsByFollowerId(@Param("userId") UUID userId);

    @Modifying
    @Query("update UserProfile u set u.followingCount = u.followingCount + :delta where u.id = :userId")
    void adjustFollowingCount(@Param("userId") UUID userId, @Param("delta") int delta);

    @Modifying
    @Query("update UserProfile u set u.followerCount = u.followerCount + :delta where u.id = :userId")
    void adjustFollowerCount(@Param("userId") UUID userId, @Param("delta") int delta);

    @Query("select u from Follow f join UserProfile u on u.id = f.followerId where f.followeeId = :userId")
    Page<UserProfile> findFollowers(@Param("userId") UUID userId, Pageable pageable);

    @Query("select u from Follow f join UserProfile u on u.id = f.followeeId where f.followerId = :userId")
    Page<UserProfile> findFollowing(@Param("userId") UUID userId, Pageable pageable);
}
