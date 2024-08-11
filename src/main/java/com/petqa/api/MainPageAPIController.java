package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.mapping.MainPageDTO;
import com.petqa.service.mainPage.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/MainPage")
public class MainPageAPIController {
    private final MainPageService mainPageService;

    @GetMapping("")//Parameter의 userId값으로 조회
    public ApiResponse<MainPageDTO.MainPageResponseDTO> getMainPage(@RequestParam Long userId) {
        MainPageDTO.MainPageResponseDTO mainPageInfo = mainPageService.getMainPageInfo(userId);
        //MainPageService의 getMainPageInfo로 정보 가져옴
        return ApiResponse.onSuccess(mainPageInfo);
    }
}
