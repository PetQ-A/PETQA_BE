package com.petqa.security.jwt;

import com.petqa.dto.user.CustomUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final CustomUserDetails principal;
    private String accessToken;
    private String refreshToken;

    public JwtAuthenticationToken(String accessToken, String refreshToken) {
        super(null);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.principal = null;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(CustomUserDetails principal, String accessToken, String refreshToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.accessToken = null;
        this.refreshToken = null;
    }
}