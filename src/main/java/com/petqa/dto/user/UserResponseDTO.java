package com.petqa.dto.user;

import com.petqa.domain.enums.Gender;
import com.petqa.domain.enums.PetType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class UserResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UserDetail {

        private String username;

        private Integer points;

        private int questionCount;

        PetDetail petDetail;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MypageDetail {
        private String username;
        private PetDetail petDetail;
        private String token;  // JWT 토큰 필드 추가

        // PetDetail 클래스와 기타 필요한 코드들...
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PetDetail {

        private Long id;
        private String name;
        private String species;
        private PetType petType;
        private LocalDate birth;
        private String profileImage;
        private double weight;
        private Gender gender;
    }
}
