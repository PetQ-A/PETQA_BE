package com.petqa.converter;

import com.petqa.domain.Snack;
import com.petqa.domain.SnackOrder;
import com.petqa.domain.User;
import com.petqa.domain.enums.SnackOrderStatus;
import com.petqa.dto.snack.SnackOrderDTO;
import com.petqa.dto.snack.SnackOrderListDTO;
import com.petqa.dto.snack.SnackResponseDTO;

import java.time.Duration;
import java.time.LocalDateTime;

public class SnackConverter {
    public static SnackResponseDTO.SnackListDTO toSnackListDTO(Snack snack) {
        return SnackResponseDTO.SnackListDTO.builder()
                .id(snack.getId())
                .snackName(snack.getName())
                .nutrient(snack.getNutrient())
                .img(snack.getImg())
                .build();
    }

    public static SnackOrder toSnackOrder(User user, Snack snack) {
        return SnackOrder.builder()
                .user(user)
                .snack(snack)
                .status(SnackOrderStatus.BEFORE) // Assuming the order is always active
                .orderTime(LocalDateTime.now())
                .build();
    }

    public static SnackOrderListDTO.SnackOrderListResponseDTO toSnackOrderListDTO(SnackOrder snackOrder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderTime = snackOrder.getOrderTime();

        Duration duration = Duration.between(orderTime, now);

        SnackOrderStatus status;
        if (duration.toDays() < 1) {
            status = SnackOrderStatus.BEFORE;//주문한지 하루 전이면 배송전으로 표시
        } else if (duration.toDays() < 7) {
            status = SnackOrderStatus.GOING;//주문한지 일주일 이내면 배송중으로 표시
        } else {
            status = SnackOrderStatus.AFTER;//주문 일주일 이후면 배송완료로 표시
        }
        return new SnackOrderListDTO.SnackOrderListResponseDTO(
                snackOrder.getSnack().getName(),
                status,
                orderTime.toString()
        );
    }
    }

