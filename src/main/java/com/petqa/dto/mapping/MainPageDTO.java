package com.petqa.dto.mapping;

import lombok.*;

public class MainPageDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainPageResponseDTO{
        String petName;

        String petCategory;

        Long point;

        String todayQuestion;

        Boolean questionStatus;
    }
}