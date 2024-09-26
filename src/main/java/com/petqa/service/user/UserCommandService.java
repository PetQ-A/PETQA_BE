package com.petqa.service.user;

import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.auth.AuthResponseDTO;
import com.petqa.dto.user.UserRequestDTO;

public interface UserCommandService {

	AuthResponseDTO.LoginResponseDTO login(AuthRequestDTO.SocialLoginDTO socialLoginDTO);

	AuthResponseDTO.LoginResponseDTO join(UserRequestDTO.CreateUserDTO joinRequest);

	AuthResponseDTO.ReissueResponseDTO reissue(String refreshToken);

	void logout(String refreshToken);

	String duplicateCheck(String username);
}
