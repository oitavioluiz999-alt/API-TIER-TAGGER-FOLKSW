package com.tiertagger.api.dto;

public record UserDto(
        Long id,
        String username,
        String displayName,
        String role
) {
}

