package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.mainpage.MainPageDTO;
import com.petqa.service.mainPage.MainPageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/MainPage")
public class MainPageAPIController {
    private final MainPageService mainPageService;

    @GetMapping("/")//Parameter의 userId값으로 조회
    public ApiResponse<MainPageDTO.MainPageResponseDTO> getMainPage(@RequestParam Long userId) {
        MainPageDTO.MainPageResponseDTO mainPageInfo = mainPageService.getMainPageInfo(userId);
        //MainPageService의 getMainPageInfo로 정보 가져옴
        return ApiResponse.onSuccess(mainPageInfo);
    }
}
