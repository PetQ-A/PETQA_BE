package com.petqa.dto.auth;

import com.petqa.domain.enums.PetType;
import lombok.*;

public class AuthResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String accessToken;
        private String refreshToken;
        private UserInfoDTO userInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UserInfoDTO {
        private Long id;
        private Integer points;
        private PetInfoDTO petInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PetInfoDTO {
        private Long id;
        private String petName;
        private PetType petType;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReissueResponseDTO {
        private String accessToken;
        private String refreshToken;
    }
}

