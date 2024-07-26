package com.petqa.security.jwt;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.UserHandler;
import com.petqa.domain.User;
import com.petqa.dto.user.CustomUserDetails;
import com.petqa.repository.UserRepository;
import com.petqa.service.refresh.RefreshCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
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
    private final RefreshCommandService refreshCommandService;


    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        TokenPair tokenPair = (TokenPair) jwtAuthenticationToken.getCredentials();
        String accessToken = tokenPair.getAccessToken();
        String refreshToken = tokenPair.getRefreshToken();

        try {
            if (jwtUtil.isExpired(accessToken)) {
                throw new UserHandler(ErrorStatus.ACCESS_TOKEN_EXPIRED);
            }

            String socialId = jwtUtil.getSocialId(accessToken);
            String username = jwtUtil.getUsername(accessToken);

            User user = userRepository.findUserBySocialIdAndUsername(socialId, username)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            CustomUserDetails userDetails = new CustomUserDetails(user);

            // Refresh 토큰 처리
            if (jwtUtil.isExpired(refreshToken)) {
                throw new UserHandler(ErrorStatus.REFRESH_TOKEN_EXPIRED);
            }

            try {
                // 기존 코드
                refreshCommandService.addRefresh(username, socialId, refreshToken, jwtUtil.getRefreshTokenExpiration());
            } catch (Exception e) {
                log.error("Failed to save refresh token", e);
                throw new AuthenticationServiceException("Authentication failed: " + e.getMessage(), e);
            }

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