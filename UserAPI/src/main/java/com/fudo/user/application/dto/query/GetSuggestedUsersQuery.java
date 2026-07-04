package com.fudo.user.application.dto.query;

import com.fudo.shared.cqrs.Request;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetSuggestedUsersQuery implements Request<List<GetUserProfileQuery.UserProfileDto>> {

    private UUID userId;
    private int limit = 10;
}
