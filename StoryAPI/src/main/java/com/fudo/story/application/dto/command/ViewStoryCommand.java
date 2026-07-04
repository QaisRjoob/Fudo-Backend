package com.fudo.story.application.dto.command;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ViewStoryCommand implements Request<Void> {

    private UUID storyId;
    private UUID viewerId;
}
