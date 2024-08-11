package com.petqa.service.mainPage;

import com.petqa.dto.mainpage.MainPageDTO;

public interface MainPageService {
    MainPageDTO.MainPageResponseDTO getMainPageInfo(Long userId);

}
