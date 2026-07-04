package com.fudo.user.application.dto.command;

import com.fudo.shared.cqrs.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnfollowUserCommand implements Request<Void> {

    private UUID followerId;
    private UUID followeeId;
}
