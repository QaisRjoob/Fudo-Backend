package com.fudo.shared.pagination;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public final class CursorHelper {

    private CursorHelper() {
    }

    public record Cursor(Instant createdAt, UUID id) {
    }

    public static String encode(Instant createdAt, UUID id) {
        String raw = createdAt.toString() + "|" + id;
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static Cursor decode(String cursor) {
        String raw = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
        String[] parts = raw.split("\\|", 2);
        return new Cursor(Instant.parse(parts[0]), UUID.fromString(parts[1]));
    }
}
