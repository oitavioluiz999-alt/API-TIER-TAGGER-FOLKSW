package com.tiertagger.api.dto;

import java.time.OffsetDateTime;

/**
 * Envelope de resposta genérica.
 */
public record ApiMessage(
        String message,
        OffsetDateTime timestamp
) {
    public static ApiMessage of(String message) {
        return new ApiMessage(message, OffsetDateTime.now());
    }
}

