package com.tiertagger.api.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Utilitário de invalidação de caches.
 */
@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void evictLeaderboard(String modeKey) {
        Cache cache = cacheManager.getCache("leaderboards");
        if (cache != null) {
            cache.evict(modeKey.toLowerCase());
        }
    }

    public void evictMetadata() {
        Cache cache = cacheManager.getCache("metadata");
        if (cache != null) {
            cache.clear();
        }
    }

    public void clearAll() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
