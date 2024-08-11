package com.petqa.service.mainPage;

import com.petqa.dto.mapping.MainPageDTO;

public interface MainPageService {
    MainPageDTO.MainPageResponseDTO getMainPageInfo(Long userId);

}
