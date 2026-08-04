package com.tiertagger.api.dto;

/**
 * Mensagem WebSocket completa transportando um TierUpdatePayload.
 */
public record TierUpdateMessage(
        String type,
        TierUpdatePayload payload,
        long timestamp
) {
    public static TierUpdateMessage of(TierUpdatePayload payload) {
        return new TierUpdateMessage("tier_update", payload, payload.timestamp());
    }
}

