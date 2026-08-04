package com.tiertagger.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TierRemoveRequest(
        @NotBlank(message = "player é obrigatório") String player,
        @NotBlank(message = "mode é obrigatório") String mode,
        String reason
) {
}

