package com.tiertagger.api.controller;

import com.tiertagger.api.dto.HistoryEntryDto;
import com.tiertagger.api.service.HistoryService;
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
 * Endpoint de histórico de um jogador.
 */
@RestController
@RequestMapping("/api/history")
@Tag(name = "History", description = "Histórico de alterações")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{player}")
    @Operation(summary = "Retorna o histórico de tiers de um jogador")
    public ResponseEntity<List<HistoryEntryDto>> history(@PathVariable String player,
                                                         @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(historyService.getHistory(player, limit));
    }
}
