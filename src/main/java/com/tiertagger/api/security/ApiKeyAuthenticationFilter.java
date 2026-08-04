package com.tiertagger.api.security;

import com.tiertagger.api.entity.ApiKey;
import com.tiertagger.api.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;

/**
 * Filtro que aceita autenticação por API Key via header X-API-Key.
 */
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthenticationFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String apiKeyValue = request.getHeader("X-API-Key");
            if (apiKeyValue != null && !apiKeyValue.isBlank()) {
                apiKeyRepository.findByKeyValueAndEnabledTrue(apiKeyValue).ifPresent(key -> {
                    markUsed(key);
                    UserPrincipal principal = UserPrincipal.fromApiKey(key.getName(), key.getRole());
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            }
        }

        filterChain.doFilter(request, response);
    }

    private void markUsed(ApiKey key) {
        key.setLastUsedAt(OffsetDateTime.now());
        apiKeyRepository.save(key);
    }
}
