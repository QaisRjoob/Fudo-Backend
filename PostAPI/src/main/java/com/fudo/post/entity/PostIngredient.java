package com.fudo.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "post_ingredients")
public class PostIngredient {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private UUID postId;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 32)
    private String amount;

    @Column(length = 32)
    private String unit;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_ingredients_post"))
    private Post post;
}
