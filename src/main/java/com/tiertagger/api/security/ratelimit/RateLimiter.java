package com.tiertagger.api.security.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tiertagger.api.config.AppProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limiter token bucket em memória (Caffeine). Suficiente para single-instance.
 */
@Component
public class RateLimiter {

    private final AppProperties properties;
    private final Cache<String, Bucket> buckets;

    public RateLimiter(AppProperties properties) {
        this.properties = properties;
        this.buckets = Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfterAccess(Duration.ofHours(1))
                .build();
    }

    public boolean isAllowed(String key, boolean isWrite) {
        int capacity = isWrite ? properties.getRateLimit().getWriteCapacity()
                : properties.getRateLimit().getCapacity();
        Bucket bucket = buckets.get(key, k -> new Bucket(capacity));
        return bucket.tryConsume(capacity);
    }

    private static final class Bucket {
        private final AtomicInteger tokens;

        Bucket(int capacity) {
            this.tokens = new AtomicInteger(capacity);
        }

        boolean tryConsume(int capacity) {
            int current = tokens.get();
            if (current > 0) {
                return tokens.compareAndSet(current, current - 1);
            }
            // Reset suave quando esvazia (janela simples)
            return tokens.compareAndSet(0, capacity - 1);
        }
    }
}
