package com.fudo.post.features.handler.query;

import com.fudo.post.application.dto.query.GetPostQuery;
import com.fudo.post.application.dto.query.GetPostQuery.IngredientDetailDto;
import com.fudo.post.application.dto.query.GetPostQuery.PostDetailDto;
import com.fudo.post.application.dto.query.GetPostQuery.StepDetailDto;
import com.fudo.post.entity.Post;
import com.fudo.post.entity.PostIngredient;
import com.fudo.post.entity.PostMedia;
import com.fudo.post.entity.PostStep;
import com.fudo.post.entity.PostTag;
import com.fudo.post.interfaces.PostRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetPostHandler implements RequestHandler<GetPostQuery, PostDetailDto> {

    private final PostRepository posts;

    public GetPostHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailDto handle(GetPostQuery query) {
        Post post = posts.getByIdWithDetails(query.getPostId()).orElse(null);
        if (post == null) {
            return null;
        }

        PostDetailDto dto = new PostDetailDto();
        dto.setId(post.getId());
        dto.setAuthorId(post.getAuthorId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setCookingTime(post.getCookingTime());
        dto.setDifficulty(post.getDifficulty());
        dto.setServingSize(post.getServingSize());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setSaveCount(post.getSaveCount());
        dto.setMediaUrls(post.getMedia().stream()
                .sorted(Comparator.comparingInt(PostMedia::getSortOrder))
                .map(PostMedia::getUrl)
                .collect(Collectors.toList()));
        dto.setTags(post.getTags().stream().map(PostTag::getTag).collect(Collectors.toList()));
        dto.setIngredients(post.getIngredients().stream()
                .sorted(Comparator.comparingInt(PostIngredient::getSortOrder))
                .map(i -> new IngredientDetailDto(i.getName(), i.getAmount(), i.getUnit()))
                .collect(Collectors.toList()));
        dto.setSteps(post.getSteps().stream()
                .sorted(Comparator.comparingInt(PostStep::getStepNumber))
                .map(s -> new StepDetailDto(s.getStepNumber(), s.getDescription(), s.getImageUrl()))
                .collect(Collectors.toList()));
        dto.setCreatedAt(post.getCreatedAt());

        return dto;
    }
}
