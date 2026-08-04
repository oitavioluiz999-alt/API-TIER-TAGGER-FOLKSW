package com.tiertagger.api.dto;

public record LeaderboardEntry(
        String player,
        String tier,
        String tierDisplayName,
        int weight,
        String color
) {
}

