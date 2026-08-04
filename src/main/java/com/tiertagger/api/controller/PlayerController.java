package com.tiertagger.api.controller;

import com.tiertagger.api.dto.PlayerResponse;
import com.tiertagger.api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de consulta de jogadores.
 */
@RestController
@Tag(name = "Player", description = "Consulta de jogadores")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/api/player/{name}")
    @Operation(summary = "Retorna os tiers de um jogador")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable String name) {
        return ResponseEntity.ok(playerService.getPlayer(name));
    }

    @GetMapping("/api/players")
    @Operation(summary = "Lista jogadores")
    public ResponseEntity<List<PlayerResponse>> listPlayers(@RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(playerService.listPlayers(limit));
    }
}
