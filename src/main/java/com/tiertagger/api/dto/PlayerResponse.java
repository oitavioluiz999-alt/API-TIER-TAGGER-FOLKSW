package com.tiertagger.api.dto;

import java.util.List;

/**
 * Resposta completa de um jogador com todos os tiers de todas as modalidades.
 */
public record PlayerResponse(
        String name,
        String uuid,
        List<PlayerTierDto> tiers
) {
}

