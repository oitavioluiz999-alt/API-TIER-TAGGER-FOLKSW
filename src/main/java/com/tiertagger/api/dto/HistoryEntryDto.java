package com.tiertagger.api.dto;

public record HistoryEntryDto(
        Long id,
        String player,
        String mode,
        String action,
        String oldTierKey,
        String newTierKey,
        String staffName,
        String reason,
        String createdAt
) {
}

