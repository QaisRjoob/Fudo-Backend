package com.fudo.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String error, Map<String, String[]> errors, Instant timestamp, String path) {

    public static ApiError of(String error, String path) {
        return new ApiError(error, null, Instant.now(), path);
    }

    public static ApiError of(String error, Map<String, String[]> errors, String path) {
        return new ApiError(error, errors, Instant.now(), path);
    }
}
