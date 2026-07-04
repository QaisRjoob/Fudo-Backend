package com.fudo.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "post_media")
public class PostMedia {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private UUID postId;

    @Column(nullable = false)
    private String url;

    @Column(name = "media_type", length = 16)
    private String mediaType = "image";

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_media_post"))
    private Post post;
}
