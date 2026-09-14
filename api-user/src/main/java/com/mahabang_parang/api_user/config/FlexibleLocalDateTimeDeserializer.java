package com.mahabang_parang.api_user.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        text = text.trim();
        // Date only, e.g. "yyyy-MM-dd"
        if (text.length() == 10) {
            return LocalDate.parse(text).atStartOfDay();
        }
        // ISO format with T, e.g. "yyyy-MM-dd'T'HH:mm:ss"
        if (text.contains("T")) {
            return LocalDateTime.parse(text);
        }
        // Format with space, e.g. "yyyy-MM-dd HH:mm:ss"
        return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
    }
}
