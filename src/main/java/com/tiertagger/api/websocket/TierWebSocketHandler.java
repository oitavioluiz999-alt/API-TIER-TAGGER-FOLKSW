package com.tiertagger.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiertagger.api.dto.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler de WebSocket que mantém todas as sessões conectadas e envia broadcasts.
 */
@Component
public class TierWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(TierWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public TierWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("WebSocket conectado: {} (total={})", session.getId(), sessions.size());
        sendTo(session, WebSocketMessage.of("connected", Map.of("message", "Conectado ao TierTagger")));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        log.info("WebSocket desconectado: {} (total={})", session.getId(), sessions.size());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Por enquanto o servidor não processa mensagens do cliente; apenas envia.
    }

    /**
     * Envia uma mensagem para todos os clientes conectados.
     */
    public void broadcast(Object message) {
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            log.error("Falha ao serializar mensagem WebSocket", e);
            return;
        }
        TextMessage text = new TextMessage(json);
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    synchronized (session) {
                        session.sendMessage(text);
                    }
                } catch (IOException e) {
                    log.warn("Falha ao enviar para sessão {}", session.getId());
                    sessions.remove(session.getId());
                    try {
                        session.close(CloseStatus.SERVER_ERROR);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    private void sendTo(WebSocketSession session, Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.warn("Falha ao enviar handshake para {}", session.getId());
        }
    }

    public int getConnectedCount() {
        return sessions.size();
    }
}
