package com.fudo.post.features.handler.command;

import com.fudo.post.application.dto.command.CreatePostCommand;
import com.fudo.post.application.dto.command.CreatePostCommand.CreatePostResult;
import com.fudo.post.entity.Post;
import com.fudo.post.entity.PostIngredient;
import com.fudo.post.entity.PostMedia;
import com.fudo.post.entity.PostStep;
import com.fudo.post.entity.PostTag;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreatePostHandler implements RequestHandler<CreatePostCommand, CreatePostResult> {

    private final PostRepository posts;

    public CreatePostHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public CreatePostResult handle(CreatePostCommand cmd) {
        Post post = new Post();
        post.setAuthorId(cmd.getAuthorId());
        post.setTitle(cmd.getTitle().trim());
        post.setDescription(cmd.getDescription());
        post.setCookingTime(cmd.getCookingTime());
        post.setDifficulty(cmd.getDifficulty());
        post.setServingSize(cmd.getServingSize());

        List<PostMedia> media = new ArrayList<>();
        List<String> mediaUrls = cmd.getMediaUrls();
        for (int i = 0; i < mediaUrls.size(); i++) {
            PostMedia m = new PostMedia();
            m.setUrl(mediaUrls.get(i));
            m.setSortOrder(i);
            m.setPost(post);
            media.add(m);
        }
        post.setMedia(media);

        List<PostTag> tags = new ArrayList<>();
        for (String t : cmd.getTags()) {
            PostTag tag = new PostTag();
            tag.setTag(t.toLowerCase().trim());
            tag.setPost(post);
            tags.add(tag);
        }
        post.setTags(tags);

        List<PostIngredient> ingredients = new ArrayList<>();
        List<CreatePostCommand.IngredientDto> ingredientDtos = cmd.getIngredients();
        for (int i = 0; i < ingredientDtos.size(); i++) {
            CreatePostCommand.IngredientDto ing = ingredientDtos.get(i);
            PostIngredient ingredient = new PostIngredient();
            ingredient.setName(ing.getName());
            ingredient.setAmount(ing.getAmount());
            ingredient.setUnit(ing.getUnit());
            ingredient.setSortOrder(i);
            ingredient.setPost(post);
            ingredients.add(ingredient);
        }
        post.setIngredients(ingredients);

        List<PostStep> steps = new ArrayList<>();
        for (CreatePostCommand.StepDto s : cmd.getSteps()) {
            PostStep step = new PostStep();
            step.setStepNumber(s.getStepNumber());
            step.setDescription(s.getDescription());
            step.setImageUrl(s.getImageUrl());
            step.setPost(post);
            steps.add(step);
        }
        post.setSteps(steps);

        post = posts.add(post);
        return new CreatePostResult(post.getId());
    }
}
