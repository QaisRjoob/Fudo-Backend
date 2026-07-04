package com.fudo.post.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreatePostCommand implements Request<CreatePostCommand.CreatePostResult> {

    private UUID authorId;

    @NotBlank
    @Size(max = 256)
    private String title;

    @Size(max = 2000)
    private String description;

    @Size(max = 64)
    private String cookingTime;

    @Size(max = 32)
    private String difficulty;

    private int servingSize = 1;

    private List<String> mediaUrls = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private List<IngredientDto> ingredients = new ArrayList<>();

    private List<StepDto> steps = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDto {
        private String name;
        private String amount;
        private String unit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepDto {
        private int stepNumber;
        private String description;
        private String imageUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreatePostResult {
        private UUID postId;
    }
}
