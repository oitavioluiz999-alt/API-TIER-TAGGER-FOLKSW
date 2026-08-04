package com.tiertagger.api.controller;

import com.tiertagger.api.dto.ApiMessage;
import com.tiertagger.api.dto.LoginRequest;
import com.tiertagger.api.dto.LoginResponse;
import com.tiertagger.api.dto.LogoutRequest;
import com.tiertagger.api.dto.RefreshRequest;
import com.tiertagger.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de autenticação: POST /api/auth/login, /logout, /refresh.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticação JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna tokens JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest http) {
        LoginResponse response = authService.login(request, clientIp(http), http.getHeader("User-Agent"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renova o access token usando refresh token")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request,
                                                 HttpServletRequest http) {
        LoginResponse response = authService.refresh(request.refreshToken(), clientIp(http), http.getHeader("User-Agent"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoga a sessão (refresh token)")
    public ResponseEntity<ApiMessage> logout(@RequestBody(required = false) LogoutRequest request) {
        if (request != null) {
            authService.logout(request.refreshToken());
        }
        return ResponseEntity.ok(ApiMessage.of("Logout realizado"));
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
