package com.petqa.security.jwt;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.TokenHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.UserHandler;
import com.petqa.domain.User;
import com.petqa.dto.user.CustomUserDetails;
import com.petqa.repository.RefreshRepository;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;


    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        TokenPair tokenPair = (TokenPair) jwtAuthenticationToken.getCredentials();
        String accessToken = tokenPair.getAccessToken();
        String refreshToken = tokenPair.getRefreshToken();

        try {
            if (jwtUtil.isExpired(accessToken)) {
                throw new TokenHandler(ErrorStatus.ACCESS_TOKEN_EXPIRED);
            }

            String socialId = jwtUtil.getSocialId(accessToken);
            String username = jwtUtil.getUsername(accessToken);

            User user = userRepository.findUserBySocialIdAndUsername(socialId, username)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            CustomUserDetails userDetails = new CustomUserDetails(user);

            // Refresh 토큰 처리
            System.out.println("refreshToken = " + refreshToken);
            if (jwtUtil.isExpired(refreshToken)) {
                throw new UserHandler(ErrorStatus.REFRESH_TOKEN_EXPIRED);
            }

            // 기존 코드
            refreshRepository.findByRefresh(refreshToken)
                    .orElseThrow(() -> new TokenHandler(ErrorStatus.REFRESH_TOKEN_NOT_FOUND));

            return new JwtAuthenticationToken(userDetails, accessToken, refreshToken, userDetails.getAuthorities());
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }


}