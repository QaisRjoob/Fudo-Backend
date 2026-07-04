package com.fudo.post.application.dto.command;

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
public class DeletePostCommand implements Request<Void> {

    private UUID postId;
    private UUID requestingUserId;
    private boolean isAdmin;
}
