package com.fudo.interaction.application.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeStatusDto {
    // Lombok generates isLiked()/isSaved() getters for these either way, but Jackson's
    // bean-property introspection strips the "is" off an isXxx() getter when deriving the
    // JSON key, so without an override this would serialize as "liked"/"saved" rather than
    // the original C# LikeStatusDto's IsLiked/IsSaved -> "isLiked"/"isSaved".
    @JsonProperty("isLiked")
    private boolean liked;

    @JsonProperty("isSaved")
    private boolean saved;
}
