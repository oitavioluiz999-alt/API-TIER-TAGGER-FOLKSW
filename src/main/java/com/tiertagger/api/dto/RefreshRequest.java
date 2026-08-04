package com.tiertagger.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "refreshToken é obrigatório") String refreshToken
) {
}

