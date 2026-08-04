package com.tiertagger.api.dto;

import java.util.List;

public record LeaderboardResponse(
        String mode,
        int total,
        List<LeaderboardEntry> players
) {
}

