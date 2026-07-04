package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.SavePostCommand;
import com.fudo.interaction.interfaces.SaveRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class SavePostHandler implements RequestHandler<SavePostCommand, Void> {

    private final SaveRepository saves;

    public SavePostHandler(SaveRepository saves) {
        this.saves = saves;
    }

    @Override
    public Void handle(SavePostCommand cmd) {
        saves.save(cmd.getPostId(), cmd.getUserId());
        return null;
    }
}
