package com.petqa.service.mypage;

import com.petqa.domain.User;
import com.petqa.dto.mypage.MypageRequestDTO;
import com.petqa.dto.user.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MypageService {

    UserResponseDTO.UserDetail showMyInfo(User currentUser);

    UserResponseDTO.MypageDetail modifyMyInfo(User currentUser, MypageRequestDTO request, MultipartFile profileImage);
}
