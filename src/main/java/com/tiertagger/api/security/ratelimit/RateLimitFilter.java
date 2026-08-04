package com.tiertagger.api.security.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiertagger.api.config.AppProperties;
import com.tiertagger.api.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de rate limit baseado em IP usando Redis ou fallback em memória.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiter rateLimiter;
    private final ObjectMapper objectMapper;
    private final AppProperties properties;

    public RateLimitFilter(RateLimiter rateLimiter, ObjectMapper objectMapper, AppProperties properties) {
        this.rateLimiter = rateLimiter;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Não limita /actuator/health para não derrubar o healthcheck
        if (request.getRequestURI().startsWith("/actuator") || request.getRequestURI().startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = resolveClientIp(request);
        boolean isWrite = "POST".equalsIgnoreCase(request.getMethod())
                || "PUT".equalsIgnoreCase(request.getMethod())
                || "PATCH".equalsIgnoreCase(request.getMethod())
                || "DELETE".equalsIgnoreCase(request.getMethod());

        if (rateLimiter.isAllowed(ip, isWrite)) {
            filterChain.doFilter(request, response);
} else {
            int status = 429;
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse body = ErrorResponse.of(
                    status,
                    "Too Many Requests",
                    "Muitas requisições. Aguarde um instante e tente novamente.",
                    request.getRequestURI());
            objectMapper.writeValue(response.getOutputStream(), body);
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }
}
