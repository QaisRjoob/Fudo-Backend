package com.fudo.interaction.application.dto.command;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UnsavePostCommand implements Request<Void> {
    private UUID postId;
    private UUID userId;
}
