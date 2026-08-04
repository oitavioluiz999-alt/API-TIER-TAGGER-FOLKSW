package com.tiertagger.api.dto;

public record ModeResponse(
        String key,
        String displayName,
        String icon,
        int sortOrder,
        boolean enabled
) {
}

