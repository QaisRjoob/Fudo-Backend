package com.fudo.interaction.features.handler.query;

import com.fudo.interaction.application.dto.query.GetLikeStatusQuery;
import com.fudo.interaction.application.dto.query.LikeStatusDto;
import com.fudo.interaction.interfaces.LikeRepository;
import com.fudo.interaction.interfaces.SaveRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class GetLikeStatusHandler implements RequestHandler<GetLikeStatusQuery, LikeStatusDto> {

    private final LikeRepository likes;
    private final SaveRepository saves;

    public GetLikeStatusHandler(LikeRepository likes, SaveRepository saves) {
        this.likes = likes;
        this.saves = saves;
    }

    @Override
    public LikeStatusDto handle(GetLikeStatusQuery query) {
        boolean liked = likes.hasLiked(query.getPostId(), query.getUserId());
        boolean saved = saves.hasSaved(query.getPostId(), query.getUserId());
        return new LikeStatusDto(liked, saved);
    }
}
