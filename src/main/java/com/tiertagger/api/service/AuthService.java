package com.tiertagger.api.service;

import com.tiertagger.api.config.AppProperties;
import com.tiertagger.api.dto.LoginRequest;
import com.tiertagger.api.dto.LoginResponse;
import com.tiertagger.api.dto.UserDto;
import com.tiertagger.api.entity.AppUser;
import com.tiertagger.api.entity.Session;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.AppUserRepository;
import com.tiertagger.api.repository.SessionRepository;
import com.tiertagger.api.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Serviço de autenticação: login, logout, refresh.
 */
@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AppProperties properties;

    public AuthService(AppUserRepository userRepository,
                       SessionRepository sessionRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AppProperties properties) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String ip, String userAgent) {
        AppUser user = userRepository.findByUsernameIgnoreCase(request.username())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!user.getEnabled()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Usuário desativado");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        String access = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refresh = jwtService.generateRefreshToken(user.getId(), user.getUsername(), user.getRole());

        Session session = new Session(
                user.getId(),
                refresh,
                jwtService.getRefreshExpiry(),
                ip,
                userAgent);
        sessionRepository.save(session);

        return new LoginResponse(
                access,
                refresh,
                "Bearer",
                jwtService.getAccessTtlSeconds(),
                new UserDto(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole()));
    }

    @Transactional
    public LoginResponse refresh(String refreshToken, String ip, String userAgent) {
        if (!jwtService.isTokenValid(refreshToken, "refresh")) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");
        }

        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Sessão inválida"));

        if (session.getRevoked() || session.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Sessão revogada ou expirada");
        }

        AppUser user = userRepository.findById(session.getUserId())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        if (!user.getEnabled()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Usuário desativado");
        }

        // Revoga o token antigo (rotação de refresh)
        sessionRepository.revokeByRefreshToken(refreshToken);

        String newAccess = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String newRefresh = jwtService.generateRefreshToken(user.getId(), user.getUsername(), user.getRole());

        Session newSession = new Session(user.getId(), newRefresh, jwtService.getRefreshExpiry(), ip, userAgent);
        sessionRepository.save(newSession);

        return new LoginResponse(
                newAccess,
                newRefresh,
                "Bearer",
                jwtService.getAccessTtlSeconds(),
                new UserDto(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole()));
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            sessionRepository.revokeByRefreshToken(refreshToken);
        }
    }
}
