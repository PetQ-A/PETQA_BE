package com.petqa.dto.snack;

import lombok.*;

public class SnackResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SnackListDTO{
        Long id;

        String snackName;

        String nutrient;

        String img;
    }

}
