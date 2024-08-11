package com.petqa.service.mainPage;

import com.petqa.converter.MainPageConverter;
import com.petqa.dto.mapping.MainPageDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private final MainPageConverter mainPageConverter;

    @Override
    @Transactional
    public MainPageDTO.MainPageResponseDTO getMainPageInfo(Long userId) {
        return mainPageConverter.tomainPageResponseDTO(userId);
        //유저 정보를 넣으면 MainPageConverter를 통해 원하는 DTO로 만들어줌
    }
}
