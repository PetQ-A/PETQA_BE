package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.base.Util;
import com.petqa.domain.Refresh;
import com.petqa.repository.RefreshRepository;
import com.petqa.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Transactional
public class AuthAPIController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<String>> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(), "refresh token null", null));
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(), "refresh token expired", null));
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(), "invalid refresh token", null));
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(), "invalid refresh token", null));
        }

        String username = jwtUtil.getUsername(refresh);
        String socialId = jwtUtil.getSocialId(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, socialId, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, socialId, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, socialId, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(Util.createCookie("refresh", newRefresh));

        return ResponseEntity.ok(ApiResponse.onSuccess("Token reissued successfully"));
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
