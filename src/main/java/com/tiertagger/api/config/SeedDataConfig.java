package com.tiertagger.api.config;

import com.tiertagger.api.entity.ApiKey;
import com.tiertagger.api.entity.AppUser;
import com.tiertagger.api.repository.ApiKeyRepository;
import com.tiertagger.api.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 * Cria o usuário ADMIN inicial e uma API Key padrão se não existirem.
 */
@Configuration
public class SeedDataConfig {

    private static final Logger log = LoggerFactory.getLogger(SeedDataConfig.class);

    @Bean
    public CommandLineRunner seedAdmin(AppUserRepository userRepository,
                                       ApiKeyRepository apiKeyRepository,
                                       PasswordEncoder passwordEncoder,
                                       AppProperties properties) {
        return args -> {
            seedUser(userRepository, passwordEncoder, properties);
            seedApiKey(apiKeyRepository);
        };
    }

    private void seedUser(AppUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AppProperties properties) {
        AppProperties.SeedAdmin seed = properties.getSeedAdmin();
        if (seed.getUsername() == null || seed.getUsername().isBlank()) {
            return;
        }
        Optional<AppUser> existing = userRepository.findByUsernameIgnoreCase(seed.getUsername());
        if (existing.isPresent()) {
            return;
        }
        AppUser user = new AppUser(
                seed.getUsername().trim(),
                passwordEncoder.encode(seed.getPassword()),
                seed.getDisplayName(),
                "ADMIN");
        userRepository.save(user);
        log.info("Usuário admin padrão criado: {}", seed.getUsername());
    }

    private void seedApiKey(ApiKeyRepository apiKeyRepository) {
        if (apiKeyRepository.count() > 0) {
            return;
        }
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String keyValue = "tt_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        ApiKey key = new ApiKey("default-integration", keyValue, "STAFF");
        apiKeyRepository.save(key);
        log.info("API Key padrão criada: {}", keyValue);
    }
}
