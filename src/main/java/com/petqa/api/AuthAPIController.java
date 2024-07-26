package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.base.Util;
import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.auth.AuthResponseDTO;
import com.petqa.dto.user.UserRequestDTO;
import com.petqa.service.user.UserCommandService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthAPIController {

    private final UserCommandService userCommandService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO.UserInfoDTO>> login(@RequestBody AuthRequestDTO.SocialLoginDTO loginRequest, HttpServletResponse response) {
        AuthResponseDTO.LoginResponseDTO loginedUser = userCommandService.login(loginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("access", loginedUser.getAccessToken());

        Cookie refresh = Util.createCookie("refresh", loginedUser.getRefreshToken());
        response.addCookie(refresh);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.onSuccess(loginedUser.getUserInfo()));
    }

    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AuthResponseDTO.UserInfoDTO>> join(
            @RequestPart("joinRequest") UserRequestDTO.CreateUserDTO joinRequest,
            @RequestPart(value = "petProfileImage", required = false) MultipartFile petProfileImage,
            HttpServletResponse response) {

        joinRequest.setPetProfileImage(petProfileImage);


        AuthResponseDTO.LoginResponseDTO joinedUser = userCommandService.join(joinRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("access", joinedUser.getAccessToken());

        Cookie refresh = Util.createCookie("refresh", joinedUser.getRefreshToken());
        response.addCookie(refresh);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.onSuccess(joinedUser.getUserInfo()));
    }


    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<String>> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        AuthResponseDTO.ReissueResponseDTO reissued = userCommandService.reissue(refresh);

        response.setHeader("access", reissued.getAccessToken());
        response.addCookie(Util.createCookie("refresh", reissued.getRefreshToken()));

        return ResponseEntity.ok(ApiResponse.onSuccess("reissued"));

    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        userCommandService.logout(refreshToken);

        // 클라이언트 쪽 쿠키 삭제
        Cookie refreshCookie = new Cookie("refresh", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃 성공"));
    }
}


