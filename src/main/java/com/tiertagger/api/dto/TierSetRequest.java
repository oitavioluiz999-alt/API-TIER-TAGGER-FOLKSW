package com.tiertagger.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TierSetRequest(
        @NotBlank(message = "player é obrigatório") String player,
        @NotBlank(message = "mode é obrigatório") String mode,
        @NotBlank(message = "tier é obrigatório") String tier,
        String reason
) {
}

