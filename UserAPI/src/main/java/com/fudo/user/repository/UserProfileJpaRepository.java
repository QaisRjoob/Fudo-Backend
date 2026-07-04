package com.fudo.user.repository;

import com.fudo.user.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileJpaRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUsername(String username);

    @Query("select u from UserProfile u where lower(u.username) like lower(concat('%', :query, '%')) " +
            "or lower(u.displayName) like lower(concat('%', :query, '%'))")
    Page<UserProfile> search(@Param("query") String query, Pageable pageable);

    @Query("select u from UserProfile u where u.id not in :excludedIds order by u.followerCount desc")
    List<UserProfile> findSuggested(@Param("excludedIds") List<UUID> excludedIds, Pageable pageable);
}
