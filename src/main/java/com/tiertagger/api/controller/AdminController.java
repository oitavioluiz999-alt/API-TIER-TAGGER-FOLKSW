package com.tiertagger.api.controller;

import com.tiertagger.api.dto.ApiMessage;
import com.tiertagger.api.service.CacheService;
import com.tiertagger.api.websocket.TierWebSocketHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints administrativos (limpeza de cache, status de conexões WebSocket).
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Endpoints administrativos")
@SecurityRequirement(name = "bearer-jwt")
public class AdminController {

    private final CacheService cacheService;
    private final TierWebSocketHandler webSocketHandler;

    public AdminController(CacheService cacheService, TierWebSocketHandler webSocketHandler) {
        this.cacheService = cacheService;
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/reload")
    @Operation(summary = "Limpa todos os caches (usado pelo comando /tier reload)")
    public ResponseEntity<ApiMessage> reloadCache() {
        cacheService.clearAll();
        return ResponseEntity.ok(ApiMessage.of("Cache recarregado com sucesso"));
    }

    @PostMapping("/ws/status")
    @Operation(summary = "Retorna o número de clientes WebSocket conectados")
    public ResponseEntity<Map<String, Integer>> wsStatus() {
        return ResponseEntity.ok(Map.of("connectedClients", webSocketHandler.getConnectedCount()));
    }
}
