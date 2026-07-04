package com.fudo.story.features.handler.query;

import com.fudo.story.application.dto.query.GetStoryArchiveQuery;
import com.fudo.story.application.dto.query.StoryDto;
import com.fudo.story.entity.Story;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetStoryArchiveHandler implements RequestHandler<GetStoryArchiveQuery, List<StoryDto>> {

    private final IStoryRepository stories;

    public GetStoryArchiveHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public List<StoryDto> handle(GetStoryArchiveQuery query) {
        List<Story> storyList = stories.getArchiveByAuthor(query.getAuthorId(), query.getPage(), query.getPageSize());
        return storyList.stream().map(this::toDto).collect(Collectors.toList());
    }

    private StoryDto toDto(Story s) {
        StoryDto dto = new StoryDto();
        dto.setId(s.getId());
        dto.setMediaUrl(s.getMediaUrl());
        dto.setMediaType(s.getMediaType());
        dto.setCaption(s.getCaption());
        dto.setViewCount(s.getViewCount());
        dto.setExpiresAt(s.getExpiresAt());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}
