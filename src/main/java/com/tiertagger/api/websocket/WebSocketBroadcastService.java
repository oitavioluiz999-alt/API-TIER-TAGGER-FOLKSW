package com.tiertagger.api.websocket;

import com.tiertagger.api.dto.TierUpdateMessage;
import com.tiertagger.api.dto.TierUpdatePayload;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por transmitir atualizações de tier para todos os clientes.
 */
@Service
public class WebSocketBroadcastService {

    private final TierWebSocketHandler handler;

    public WebSocketBroadcastService(TierWebSocketHandler handler) {
        this.handler = handler;
    }

    /**
     * Dispara o broadcast de uma atualização de tier.
     */
    public void broadcastTierUpdate(String event, String player, String mode,
                                    String tierOld, String tierNew, String staff) {
        TierUpdatePayload payload = new TierUpdatePayload(
                event, player, mode, tierOld, tierNew, staff, System.currentTimeMillis());
        TierUpdateMessage message = TierUpdateMessage.of(payload);
        handler.broadcast(message);
    }
}
