package com.fudo.post.application.dto.query;

import com.fudo.shared.cqrs.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostQuery implements Request<GetPostQuery.PostDetailDto> {

    private UUID postId;

    @Getter
    @Setter
    public static class PostDetailDto {
        private UUID id;
        private UUID authorId;
        private String title;
        private String description;
        private String cookingTime;
        private String difficulty;
        private int servingSize;
        private int likeCount;
        private int commentCount;
        private int saveCount;
        private List<String> mediaUrls = new ArrayList<>();
        private List<String> tags = new ArrayList<>();
        private List<IngredientDetailDto> ingredients = new ArrayList<>();
        private List<StepDetailDto> steps = new ArrayList<>();
        private Instant createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDetailDto {
        private String name;
        private String amount;
        private String unit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepDetailDto {
        private int stepNumber;
        private String description;
        private String imageUrl;
    }
}
