package com.petqa.dto.auth;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class AuthRequestDTO {

    @Getter
    public static class SocialLoginDTO {
        @NotEmpty(message = "소셜 ID는 필수값입니다.")
        private String socialId;

        @NotEmpty(message = "유저 이름은 필수값입니다.")
        private String username;
    }

}
