package com.fudo.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

/**
 * Read-only mapping onto the "follows" table, owned and schema-managed by
 * UserAPI. Mirrors the original .NET FeedAPI's {@code FollowReadModel}
 * mapped via {@code .ToTable("Follows")}.
 */
@Entity
@Immutable
@Table(name = "follows")
public class FollowReadModel {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "follower_id", nullable = false)
    private UUID followerId;

    @Column(name = "followee_id", nullable = false)
    private UUID followeeId;

    protected FollowReadModel() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getFolloweeId() {
        return followeeId;
    }
}
