package com.tiertagger.api.controller;

import com.tiertagger.api.dto.PlayerResponse;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.PlayerRepository;
import com.tiertagger.api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint de busca de jogadores (prefixo do nome).
 */
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Busca de jogadores")
public class SearchController {

    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    public SearchController(PlayerRepository playerRepository, PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    @GetMapping("/{query}")
    @Operation(summary = "Busca jogadores por prefixo do nome")
    public ResponseEntity<List<PlayerResponse>> search(@PathVariable String query) {
        if (query == null || query.isBlank()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Consulta vazia");
        }
        String prefix = query.trim().toLowerCase();
        List<PlayerResponse> results = playerRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().startsWith(prefix))
                .limit(25)
                .map(p -> playerService.getPlayer(p.getName()))
                .toList();
        return ResponseEntity.ok(results);
    }
}
