package com.performworld.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),              // "yyyy-MM-dd"
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),        // "yyyy-MM-dd HH:mm"
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")      // "yyyy-MM-dd HH:mm:ss"
    };

    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText().trim();
        if (text.isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                if (formatter.equals(FORMATTERS[0])) {
                    return LocalDateTime.parse(text + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
                return LocalDateTime.parse(text, formatter);
            } catch (Exception e) {
                System.err.println("Failed to parse date: " + text + " with format: " + formatter);
                continue;
            }
        }

        // 모든 format 실패 시 예외 발생
        throw new IOException("Unable to parse date: " + text);
    }
}