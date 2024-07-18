package com.petqa.dto.auth;


import lombok.Getter;

public class AuthRequestDTO {

    @Getter
    public static class KakaoLoginDTO {
        private Integer socialId;

        private String username;
    }

}
