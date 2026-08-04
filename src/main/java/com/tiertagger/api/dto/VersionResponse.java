package com.tiertagger.api.dto;

public record VersionResponse(
        String apiVersion,
        String latestClientVersion,
        String downloadUrl,
        boolean updateAvailable
) {
}

