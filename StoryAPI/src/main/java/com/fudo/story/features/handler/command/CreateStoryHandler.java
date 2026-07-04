package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.CreateStoryCommand;
import com.fudo.story.application.dto.command.CreateStoryCommand.CreateStoryResult;
import com.fudo.story.entity.Story;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class CreateStoryHandler implements RequestHandler<CreateStoryCommand, CreateStoryResult> {

    private final IStoryRepository stories;

    public CreateStoryHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public CreateStoryResult handle(CreateStoryCommand cmd) {
        Story story = new Story();
        story.setAuthorId(cmd.getAuthorId());
        story.setMediaUrl(cmd.getMediaUrl());
        story.setMediaType(cmd.getMediaType());
        story.setCaption(cmd.getCaption());
        story.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        stories.add(story);

        return new CreateStoryResult(story.getId(), story.getExpiresAt());
    }
}
