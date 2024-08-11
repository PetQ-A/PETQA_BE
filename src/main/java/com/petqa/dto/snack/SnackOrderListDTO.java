package com.petqa.dto.snack;

import com.petqa.domain.enums.SnackOrderStatus;
import lombok.*;

public class SnackOrderListDTO {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SnackOrderListResponseDTO {
        String snackName;

        SnackOrderStatus status;

        String orderTime;
    }
}
