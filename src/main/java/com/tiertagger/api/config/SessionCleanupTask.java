package com.tiertagger.api.config;

import com.tiertagger.api.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Limpa sessões expiradas periodicamente.
 */
@Component
public class SessionCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(SessionCleanupTask.class);

    private final SessionRepository sessionRepository;

    public SessionCleanupTask(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(fixedDelay = 3_600_000) // 1h
    public void cleanupExpiredSessions() {
        try {
            int deleted = sessionRepository.deleteExpired(OffsetDateTime.now());
            if (deleted > 0) {
                log.info("Sessões expiradas removidas: {}", deleted);
            }
        } catch (Exception e) {
            log.warn("Falha ao limpar sessões expiradas", e);
        }
    }
}
