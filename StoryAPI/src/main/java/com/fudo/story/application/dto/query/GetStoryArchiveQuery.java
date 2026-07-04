package com.fudo.story.application.dto.query;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetStoryArchiveQuery implements Request<List<StoryDto>> {

    private UUID authorId;
    private int page = 1;
    private int pageSize = 30;
}
