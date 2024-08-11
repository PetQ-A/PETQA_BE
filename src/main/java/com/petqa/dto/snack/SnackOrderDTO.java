package com.petqa.dto.snack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SnackOrderDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SnackOderRequestDTO{
        Long userId;

        Long snackId;
    }
}
