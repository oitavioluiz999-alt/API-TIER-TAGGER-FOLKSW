package com.tiertagger.api.dto;

/**
 * Representa o tier de um jogador em uma modalidade específica.
 */
public record PlayerTierDto(
        String mode,
        String modeDisplayName,
        String tier,
        String tierDisplayName,
        int weight,
        String color,
        String updatedAt
) {
}

