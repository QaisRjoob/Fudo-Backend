package com.fudo.interaction.features.handler.command;

import com.fudo.interaction.application.dto.command.UnsavePostCommand;
import com.fudo.interaction.interfaces.SaveRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class UnsavePostHandler implements RequestHandler<UnsavePostCommand, Void> {

    private final SaveRepository saves;

    public UnsavePostHandler(SaveRepository saves) {
        this.saves = saves;
    }

    @Override
    public Void handle(UnsavePostCommand cmd) {
        saves.unsave(cmd.getPostId(), cmd.getUserId());
        return null;
    }
}
