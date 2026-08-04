package com.tiertagger.api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuração de cache Caffeine.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String PLAYER_CACHE = "players";
    public static final String METADATA_CACHE = "metadata";
    public static final String LEADERBOARD_CACHE = "leaderboards";

    private final AppProperties properties;

    public CacheConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                PLAYER_CACHE, METADATA_CACHE, LEADERBOARD_CACHE);

        manager.registerCustomCache(PLAYER_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(properties.getCache().getPlayerTtlMinutes()))
                        .maximumSize(properties.getCache().getMaxSize())
                        .build());

        manager.registerCustomCache(METADATA_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(properties.getCache().getMetadataTtlMinutes()))
                        .maximumSize(properties.getCache().getMaxSize())
                        .build());

        manager.registerCustomCache(LEADERBOARD_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(properties.getCache().getMetadataTtlMinutes()))
                        .maximumSize(properties.getCache().getMaxSize())
                        .build());

        manager.setAllowNullValues(false);
        return manager;
    }
}

