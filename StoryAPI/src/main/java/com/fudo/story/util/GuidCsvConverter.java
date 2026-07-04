package com.fudo.story.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mirrors the EF Core value converter on {@code StoryHighlight.StoryIds}:
 * {@code v => string.Join(',', v)} / {@code v => v.Split(',', RemoveEmptyEntries).Select(Guid.Parse).ToList()}.
 */
@Converter
public class GuidCsvConverter implements AttributeConverter<List<UUID>, String> {

    @Override
    public String convertToDatabaseColumn(List<UUID> attribute) {
        if (attribute == null) {
            return "";
        }
        return attribute.stream().map(UUID::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<UUID> convertToEntityAttribute(String dbData) {
        List<UUID> result = new ArrayList<>();
        if (dbData == null || dbData.isEmpty()) {
            return result;
        }
        for (String part : dbData.split(",")) {
            if (!part.isEmpty()) {
                result.add(UUID.fromString(part));
            }
        }
        return result;
    }
}
