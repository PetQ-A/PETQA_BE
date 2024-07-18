package com.petqa.service.user;

import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.user.UserResponseDTO;

public interface UserCommandService {

    UserResponseDTO.UserDetail kakaoLogin(AuthRequestDTO.KakaoLoginDTO kakaoRequest);


}
