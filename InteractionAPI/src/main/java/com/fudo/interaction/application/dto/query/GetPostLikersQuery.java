package com.fudo.interaction.application.dto.query;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetPostLikersQuery implements Request<List<UUID>> {
    private UUID postId;
    private int page = 1;
    private int pageSize = 20;
}
