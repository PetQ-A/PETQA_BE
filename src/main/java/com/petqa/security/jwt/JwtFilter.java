package com.petqa.security.jwt;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.TokenHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    

    public JwtFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(request.getRequestURI()) || "/join".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");
        String refreshToken = request.getHeader("refresh");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(accessToken, refreshToken);

            // AuthenticationManager를 사용하여 인증 수행
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증 성공 시 SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            throw new TokenHandler(ErrorStatus.ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}