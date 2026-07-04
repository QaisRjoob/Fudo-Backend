package com.fudo.identity.features.handler.query;

import com.fudo.identity.application.dto.query.GetUserQuery;
import com.fudo.identity.application.dto.query.GetUserQuery.UserDto;
import com.fudo.identity.entity.ApplicationUser;
import com.fudo.identity.interfaces.UserRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class GetUserHandler implements RequestHandler<GetUserQuery, UserDto> {

    private final UserRepository users;

    public GetUserHandler(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDto handle(GetUserQuery query) {
        return users.getById(query.getUserId())
                .map(this::toDto)
                .orElse(null);
    }

    private UserDto toDto(ApplicationUser user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getDisplayName(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getGender(),
                user.isActive(),
                user.isAdmin(),
                user.getCreatedAt());
    }
}
