package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.ViewStoryCommand;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class ViewStoryHandler implements RequestHandler<ViewStoryCommand, Void> {

    private final IStoryRepository stories;

    public ViewStoryHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public Void handle(ViewStoryCommand cmd) {
        stories.recordView(cmd.getStoryId(), cmd.getViewerId());
        return null;
    }
}
