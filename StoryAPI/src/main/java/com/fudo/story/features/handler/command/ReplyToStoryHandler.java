package com.fudo.story.features.handler.command;

import com.fudo.story.application.dto.command.ReplyToStoryCommand;
import com.fudo.story.entity.StoryReply;
import com.fudo.story.interfaces.IStoryRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ReplyToStoryHandler implements RequestHandler<ReplyToStoryCommand, Void> {

    private final IStoryRepository stories;

    public ReplyToStoryHandler(IStoryRepository stories) {
        this.stories = stories;
    }

    @Override
    public Void handle(ReplyToStoryCommand cmd) {
        stories.getById(cmd.getStoryId())
                .orElseThrow(() -> new NotFoundException("Story", cmd.getStoryId()));

        StoryReply reply = new StoryReply();
        reply.setStoryId(cmd.getStoryId());
        reply.setSenderId(cmd.getSenderId());
        reply.setContent(cmd.getContent());

        stories.addReply(reply);
        return null;
    }
}
