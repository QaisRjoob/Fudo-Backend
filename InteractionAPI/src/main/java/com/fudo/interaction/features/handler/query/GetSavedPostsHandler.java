package com.fudo.interaction.features.handler.query;

import com.fudo.interaction.application.dto.query.GetSavedPostsQuery;
import com.fudo.interaction.interfaces.SaveRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetSavedPostsHandler implements RequestHandler<GetSavedPostsQuery, PagedResult<UUID>> {

    private final SaveRepository saves;

    public GetSavedPostsHandler(SaveRepository saves) {
        this.saves = saves;
    }

    @Override
    public PagedResult<UUID> handle(GetSavedPostsQuery query) {
        return saves.getSavedPostIds(query.getUserId(), query.getPage(), query.getPageSize());
    }
}
