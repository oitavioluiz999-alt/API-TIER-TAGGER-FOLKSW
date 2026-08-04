package com.tiertagger.api.controller;

import com.tiertagger.api.dto.ApiMessage;
import com.tiertagger.api.dto.TierRemoveRequest;
import com.tiertagger.api.dto.TierSetRequest;
import com.tiertagger.api.security.UserPrincipal;
import com.tiertagger.api.service.TierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de escrita de tiers.
 */
@RestController
@RequestMapping("/api/tier")
@Tag(name = "Tier", description = "Definição e remoção de tiers (staff)")
@SecurityRequirement(name = "bearer-jwt")
public class TierController {

    private final TierService tierService;

    public TierController(TierService tierService) {
        this.tierService = tierService;
    }

    @PostMapping("/set")
    @Operation(summary = "Define/atualiza o tier de um jogador em uma modalidade")
    public ResponseEntity<ApiMessage> setTier(@Valid @RequestBody TierSetRequest request,
                                              @AuthenticationPrincipal UserPrincipal principal,
                                              HttpServletRequest http) {
        String staff = principal != null ? principal.getUsername() : "api";
        tierService.setTier(request, staff, clientIp(http));
        return ResponseEntity.ok(ApiMessage.of("Tier definido com sucesso"));
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove o tier de um jogador em uma modalidade")
    public ResponseEntity<ApiMessage> removeTier(@Valid @RequestBody TierRemoveRequest request,
                                                 @AuthenticationPrincipal UserPrincipal principal,
                                                 HttpServletRequest http) {
        String staff = principal != null ? principal.getUsername() : "api";
        tierService.removeTier(request, staff, clientIp(http));
        return ResponseEntity.ok(ApiMessage.of("Tier removido com sucesso"));
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
