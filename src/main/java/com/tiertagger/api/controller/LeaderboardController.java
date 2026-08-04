package com.tiertagger.api.controller;

import com.tiertagger.api.dto.LeaderboardResponse;
import com.tiertagger.api.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de leaderboard por modalidade.
 */
@RestController
@RequestMapping("/api/leaderboard")
@Tag(name = "Leaderboard", description = "Ranking por modalidade")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/{mode}")
    @Operation(summary = "Retorna o leaderboard de uma modalidade")
    public ResponseEntity<LeaderboardResponse> leaderboard(@PathVariable String mode) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(mode));
    }
}
