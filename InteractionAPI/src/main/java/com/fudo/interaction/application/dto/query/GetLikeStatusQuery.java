package com.fudo.interaction.application.dto.query;

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
public class GetLikeStatusQuery implements Request<LikeStatusDto> {
    private UUID postId;
    private UUID userId;
}
