package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.base.Util;
import com.petqa.domain.User;
import com.petqa.dto.diary.DiaryRequestDTO;
import com.petqa.dto.diary.DiaryResponseDTO;
import com.petqa.dto.mypage.MypageRequestDTO;
import com.petqa.dto.user.UserResponseDTO;
import com.petqa.service.mypage.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static com.petqa.base.Util.getCurrentUser;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageAPIController {

    private final MypageService mypageService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO.UserDetail>> showMyPage(){

        User user = getCurrentUser();
        UserResponseDTO.UserDetail info = mypageService.showMyInfo(user);

        return ResponseEntity.ok(ApiResponse.onSuccess(info));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO.MypageDetail>> modifyMyPage(@RequestPart("request") MypageRequestDTO request,
                                                                                @RequestPart(value = "petProfileImage", required = false) MultipartFile profileImage){
        User user = getCurrentUser();
        UserResponseDTO.MypageDetail info = mypageService.modifyMyInfo(user, request, profileImage);

        return ResponseEntity.ok(ApiResponse.onSuccess(info));
    }

}
