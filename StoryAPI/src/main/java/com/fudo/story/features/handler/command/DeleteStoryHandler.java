package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.DeleteStoryCommand;
import com.fudo.story.entity.Story;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DeleteStoryHandler implements RequestHandler<DeleteStoryCommand, Void> {

    private final IStoryRepository stories;

    public DeleteStoryHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public Void handle(DeleteStoryCommand cmd) {
        Story story = stories.getById(cmd.getStoryId())
                .orElseThrow(() -> new NotFoundException("Story", cmd.getStoryId()));

        if (!story.getAuthorId().equals(cmd.getRequestingUserId())) {
            throw new ForbiddenException("You can only delete your own stories.");
        }

        stories.delete(cmd.getStoryId());
        return null;
    }
}
