package com.petqa.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.base.Util;
import com.petqa.domain.Refresh;
import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.auth.CustomUserDetails;
import com.petqa.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    private final JWTUtil jwtUtil;


    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();

        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // JSON 요청 본문을 LoginRequest 객체로 파싱
            AuthRequestDTO.KakaoLoginDTO loginRequest = objectMapper.readValue(request.getInputStream(), AuthRequestDTO.KakaoLoginDTO.class);


            Integer socialId = loginRequest.getSocialId();
            String username = loginRequest.getUsername();

            System.out.println("socialId = " + socialId);
            System.out.println("username = " + username);
            // kakaoId를 principal로, username을 credentials로 사용
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(socialId, username, null);

            // AuthenticationManager로 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            System.out.println("Failed to parse authentication request body");
            throw new RuntimeException("Failed to parse authentication request body", e);
        } catch (RuntimeException e) {
            System.out.println("Failed to authenticate");
            throw new RuntimeException("Failed to authenticate", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String socialId = customUserDetails.getSocialId();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, socialId, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, socialId, 86400000L);

        addRefreshEntity(username, socialId, refresh, 86400000L);


        //응답 설정
        response.setHeader("access", access);
        response.addCookie(Util.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<String> apiResponse = ApiResponse.onFailure(
                ErrorStatus.UNAUTHORIZED.getCode(),
                ErrorStatus.UNAUTHORIZED.getMessage(),
                null
        );

        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRefreshEntity(String username, String socialId, String refresh, Long expiredMs) {
        try {
            Date date = new Date(System.currentTimeMillis() + expiredMs);
            Refresh refreshToken = Refresh.builder()
                    .username(username)
                    .socialId(socialId)
                    .refresh(refresh)
                    .expiration(date)
                    .build();
            refreshRepository.save(refreshToken);
            System.out.println("Refresh token saved successfully: " + refresh);
        } catch (Exception e) {
            System.err.println("Error saving refresh token: " + e.getMessage());
            e.printStackTrace();
        }
    }




}