package com.fudo.story.features.handler.query;

import com.fudo.story.application.dto.query.GetStoriesQuery;
import com.fudo.story.application.dto.query.GetStoriesQuery.StoryGroupDto;
import com.fudo.story.application.dto.query.StoryDto;
import com.fudo.story.entity.Story;
import com.fudo.story.entity.StoryView;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetStoriesHandler implements RequestHandler<GetStoriesQuery, List<StoryGroupDto>> {

    private final IStoryRepository stories;

    public GetStoriesHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public List<StoryGroupDto> handle(GetStoriesQuery query) {
        List<Story> storyList = stories.getFeedStories(query.getAuthorIds());

        Map<UUID, List<Story>> byAuthor = storyList.stream()
                .collect(Collectors.groupingBy(Story::getAuthorId, LinkedHashMap::new, Collectors.toList()));

        return byAuthor.entrySet().stream().map(entry -> {
            StoryGroupDto group = new StoryGroupDto();
            group.setAuthorId(entry.getKey());
            group.setAuthorUsername("");
            group.setHasUnseen(entry.getValue().stream()
                    .anyMatch(s -> s.getViews().stream().noneMatch(v -> v.getViewerId().equals(query.getRequestingUserId()))));
            group.setStories(entry.getValue().stream()
                    .sorted(Comparator.comparing(Story::getCreatedAt))
                    .map(s -> toDto(s, query.getRequestingUserId()))
                    .collect(Collectors.toList()));
            return group;
        }).collect(Collectors.toList());
    }

    private StoryDto toDto(Story s, UUID requestingUserId) {
        StoryDto dto = new StoryDto();
        dto.setId(s.getId());
        dto.setMediaUrl(s.getMediaUrl());
        dto.setMediaType(s.getMediaType());
        dto.setCaption(s.getCaption());
        dto.setViewed(s.getViews().stream().anyMatch(v -> v.getViewerId().equals(requestingUserId)));
        dto.setViewCount(s.getViewCount());
        dto.setExpiresAt(s.getExpiresAt());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}
