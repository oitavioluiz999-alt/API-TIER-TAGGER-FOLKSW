package com.tiertagger.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades customizadas do TierTagger agrupadas sob o prefixo "tiertagger".
 */
@ConfigurationProperties(prefix = "tiertagger")
public class AppProperties {

    private String appVersion = "1.0.0";
    private String clientLatestVersion = "1.0.0";
    private String clientDownloadUrl = "";

    private Jwt jwt = new Jwt();
    private SeedAdmin seedAdmin = new SeedAdmin();
    private Cors cors = new Cors();
    private RateLimit rateLimit = new RateLimit();
    private Cache cache = new Cache();

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getClientLatestVersion() {
        return clientLatestVersion;
    }

    public void setClientLatestVersion(String clientLatestVersion) {
        this.clientLatestVersion = clientLatestVersion;
    }

    public String getClientDownloadUrl() {
        return clientDownloadUrl;
    }

    public void setClientDownloadUrl(String clientDownloadUrl) {
        this.clientDownloadUrl = clientDownloadUrl;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public SeedAdmin getSeedAdmin() {
        return seedAdmin;
    }

    public void setSeedAdmin(SeedAdmin seedAdmin) {
        this.seedAdmin = seedAdmin;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public static class Jwt {
        private String secret;
        private long accessTtlMinutes = 15;
        private long refreshTtlDays = 7;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getAccessTtlMinutes() {
            return accessTtlMinutes;
        }

        public void setAccessTtlMinutes(long accessTtlMinutes) {
            this.accessTtlMinutes = accessTtlMinutes;
        }

        public long getRefreshTtlDays() {
            return refreshTtlDays;
        }

        public void setRefreshTtlDays(long refreshTtlDays) {
            this.refreshTtlDays = refreshTtlDays;
        }
    }

    public static class SeedAdmin {
        private String username = "admin";
        private String password = "admin123";
        private String displayName = "Administrador";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    public static class Cors {
        private String allowedOrigins = "http://localhost:3000,http://localhost:8080";

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String[] allowedOriginsArray() {
            if (allowedOrigins == null || allowedOrigins.isBlank() || "*".equals(allowedOrigins.trim())) {
                return new String[]{"*"};
            }
            return java.util.Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }
    }

    public static class RateLimit {
        private int capacity = 120;
        private int windowMinutes = 1;
        private int writeCapacity = 20;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getWindowMinutes() {
            return windowMinutes;
        }

        public void setWindowMinutes(int windowMinutes) {
            this.windowMinutes = windowMinutes;
        }

        public int getWriteCapacity() {
            return writeCapacity;
        }

        public void setWriteCapacity(int writeCapacity) {
            this.writeCapacity = writeCapacity;
        }
    }

    public static class Cache {
        private int playerTtlMinutes = 10;
        private int metadataTtlMinutes = 60;
        private int maxSize = 10_000;

        public int getPlayerTtlMinutes() {
            return playerTtlMinutes;
        }

        public void setPlayerTtlMinutes(int playerTtlMinutes) {
            this.playerTtlMinutes = playerTtlMinutes;
        }

        public int getMetadataTtlMinutes() {
            return metadataTtlMinutes;
        }

        public void setMetadataTtlMinutes(int metadataTtlMinutes) {
            this.metadataTtlMinutes = metadataTtlMinutes;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
    }
}

