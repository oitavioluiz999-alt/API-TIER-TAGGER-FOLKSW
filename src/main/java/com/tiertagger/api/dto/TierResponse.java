package com.tiertagger.api.dto;

public record TierResponse(
        String key,
        String displayName,
        int weight,
        String icon,
        String color,
        boolean enabled
) {
}

