package com.petqa.service.user;


import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.user.UserResponseDTO;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;


    @Override
    public UserResponseDTO.UserDetail kakaoLogin(AuthRequestDTO.KakaoLoginDTO kakaoRequest) {
        return null;
    }
}
