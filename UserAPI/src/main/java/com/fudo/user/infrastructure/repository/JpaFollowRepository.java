package com.fudo.user.infrastructure.repository;

import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.entity.Follow;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.FollowRepository;
import com.fudo.user.repository.FollowJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaFollowRepository implements FollowRepository {

    private final FollowJpaRepository jpaRepository;

    public JpaFollowRepository(FollowJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean isFollowing(UUID followerId, UUID followeeId) {
        return jpaRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    @Transactional
    public void follow(UUID followerId, UUID followeeId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFolloweeId(followeeId);
        jpaRepository.save(follow);

        jpaRepository.adjustFollowingCount(followerId, 1);
        jpaRepository.adjustFollowerCount(followeeId, 1);
    }

    @Override
    @Transactional
    public void unfollow(UUID followerId, UUID followeeId) {
        long deleted = jpaRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);

        if (deleted > 0) {
            jpaRepository.adjustFollowingCount(followerId, -1);
            jpaRepository.adjustFollowerCount(followeeId, -1);
        }
    }

    @Override
    public PagedResult<UserProfile> getFollowers(UUID userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("followerCount").descending());
        Page<UserProfile> result = jpaRepository.findFollowers(userId, pageable);
        return new PagedResult<>(result.getContent(), (int) result.getTotalElements(), page, pageSize);
    }

    @Override
    public PagedResult<UserProfile> getFollowing(UUID userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("followerCount").descending());
        Page<UserProfile> result = jpaRepository.findFollowing(userId, pageable);
        return new PagedResult<>(result.getContent(), (int) result.getTotalElements(), page, pageSize);
    }

    @Override
    public List<UUID> getFollowingIds(UUID userId) {
        return jpaRepository.findFolloweeIdsByFollowerId(userId);
    }
}
