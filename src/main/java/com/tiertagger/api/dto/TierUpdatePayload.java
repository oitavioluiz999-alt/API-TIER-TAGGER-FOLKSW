package com.tiertagger.api.dto;

/**
 * Payload da mensagem WebSocket de atualização de tier.
 */
public record TierUpdatePayload(
        String event,
        String player,
        String mode,
        String tierOld,
        String tierNew,
        String staff,
        long timestamp
) {
}

