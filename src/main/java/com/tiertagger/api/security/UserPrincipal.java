package com.tiertagger.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Autenticação principal do usuário autenticado.
 */
public final class UserPrincipal {

    private final Long userId;
    private final String username;
    private final String role;
    private final String authType;

    private UserPrincipal(Long userId, String username, String role, String authType) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.authType = authType;
    }

    public static UserPrincipal fromJwt(Long userId, String username, String role) {
        return new UserPrincipal(userId, username, role, "JWT");
    }

    public static UserPrincipal fromApiKey(String name, String role) {
        return new UserPrincipal(null, name, role, "API_KEY");
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getAuthType() {
        return authType;
    }

public Collection<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
