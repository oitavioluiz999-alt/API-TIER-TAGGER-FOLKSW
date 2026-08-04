package com.tiertagger.api.dto;

/**
 * Mensagem enviada via WebSocket para todos os clientes.
 */
public record WebSocketMessage(
        String type,
        Object payload,
        long timestamp
) {
    public static WebSocketMessage of(String type, Object payload) {
        return new WebSocketMessage(type, payload, System.currentTimeMillis());
    }
}

