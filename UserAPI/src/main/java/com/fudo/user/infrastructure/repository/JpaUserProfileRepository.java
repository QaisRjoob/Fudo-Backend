package com.fudo.user.infrastructure.repository;

import com.fudo.shared.pagination.PagedResult;
import com.fudo.user.entity.UserProfile;
import com.fudo.user.interfaces.UserProfileRepository;
import com.fudo.user.repository.FollowJpaRepository;
import com.fudo.user.repository.UserProfileJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserProfileRepository implements UserProfileRepository {

    private final UserProfileJpaRepository jpaRepository;
    private final FollowJpaRepository followJpaRepository;

    public JpaUserProfileRepository(UserProfileJpaRepository jpaRepository, FollowJpaRepository followJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.followJpaRepository = followJpaRepository;
    }

    @Override
    public Optional<UserProfile> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<UserProfile> getByUsername(String username) {
        return jpaRepository.findByUsername(username.toLowerCase());
    }

    @Override
    public void add(UserProfile profile) {
        jpaRepository.save(profile);
    }

    @Override
    public void update(UserProfile profile) {
        jpaRepository.save(profile);
    }

    @Override
    public PagedResult<UserProfile> search(String query, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("username").ascending());
        Page<UserProfile> result = jpaRepository.search(query, pageable);
        return new PagedResult<>(result.getContent(), (int) result.getTotalElements(), page, pageSize);
    }

    @Override
    public List<UserProfile> getSuggested(UUID userId, int limit) {
        List<UUID> excludedIds = new java.util.ArrayList<>(followJpaRepository.findFolloweeIdsByFollowerId(userId));
        excludedIds.add(userId);
        Pageable pageable = PageRequest.of(0, limit);
        return jpaRepository.findSuggested(excludedIds, pageable);
    }
}
