package com.fudo.story.application.dto.query;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetStoriesQuery implements Request<List<GetStoriesQuery.StoryGroupDto>> {

    private UUID requestingUserId;
    private List<UUID> authorIds = new ArrayList<>();

    @Getter
    @Setter
    public static class StoryGroupDto {
        private UUID authorId;
        private String authorUsername = "";
        private String authorAvatarUrl;
        private boolean hasUnseen;
        private List<StoryDto> stories = new ArrayList<>();
    }
}
