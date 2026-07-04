package com.fudo.interaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "post_comments",
        indexes = @Index(name = "ix_post_comments_post_id", columnList = "post_id"))
public class PostComment {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "parent_comment_id")
    private UUID parentCommentId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Self-referential relationship mirroring the original's
    // HasMany(c => c.Replies).WithOne().HasForeignKey(c => c.ParentCommentId).OnDelete(DeleteBehavior.Restrict).
    // No cascade is configured on either side so deleting a parent never cascades to its replies.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_post_comments_parent"))
    private PostComment parent;

    @OneToMany(mappedBy = "parent")
    private List<PostComment> replies = new ArrayList<>();
}
