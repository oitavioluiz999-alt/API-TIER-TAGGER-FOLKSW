package com.tiertagger.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors,
        String path,
        OffsetDateTime timestamp
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, null, path, OffsetDateTime.now());
    }
}

