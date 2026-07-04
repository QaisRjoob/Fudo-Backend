package com.fudo.interaction.features.handler.query;

import com.fudo.interaction.application.dto.query.GetPostLikersQuery;
import com.fudo.interaction.interfaces.LikeRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetPostLikersHandler implements RequestHandler<GetPostLikersQuery, List<UUID>> {

    private final LikeRepository likes;

    public GetPostLikersHandler(LikeRepository likes) {
        this.likes = likes;
    }

    @Override
    public List<UUID> handle(GetPostLikersQuery query) {
        return likes.getLikerIds(query.getPostId(), query.getPage(), query.getPageSize());
    }
}
