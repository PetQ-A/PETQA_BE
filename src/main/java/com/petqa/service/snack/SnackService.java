package com.petqa.service.snack;

import com.petqa.domain.SnackOrder;
import com.petqa.dto.snack.SnackOrderDTO;
import com.petqa.dto.snack.SnackOrderListDTO;
import com.petqa.dto.snack.SnackResponseDTO;

import java.util.List;

public interface SnackService {
    List<SnackResponseDTO.SnackListDTO> getAllSnacks();

    void OrderSnack(SnackOrderDTO.SnackOderRequestDTO request);

    List<SnackOrderListDTO.SnackOrderListResponseDTO> getOrderList(Long userId);

}
