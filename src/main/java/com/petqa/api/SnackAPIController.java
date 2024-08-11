package com.petqa.api;

import com.petqa.apiPayload.apiPayload.ApiResponse;
import com.petqa.dto.snack.SnackOrderDTO;
import com.petqa.dto.snack.SnackOrderListDTO;
import com.petqa.dto.snack.SnackResponseDTO;
import com.petqa.service.snack.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Snacks")
public class SnackAPIController {
    private final SnackService snackService;

    @GetMapping("")
    public ApiResponse<List<SnackResponseDTO.SnackListDTO>> getAllSnacks() {
        List<SnackResponseDTO.SnackListDTO> allSnacks = snackService.getAllSnacks();
        return ApiResponse.onSuccess(allSnacks);
    }
    @PostMapping("/order")
    public ApiResponse<String> Order(@RequestBody SnackOrderDTO.SnackOderRequestDTO request) {
        snackService.OrderSnack(request);
        return ApiResponse.onSuccess("success");
    }

    @GetMapping("/order/list")
    public ApiResponse<List<SnackOrderListDTO.SnackOrderListResponseDTO>> getList(@RequestParam Long userId){
        List<SnackOrderListDTO.SnackOrderListResponseDTO> orderList = snackService.getOrderList(userId);
        return ApiResponse.onSuccess(orderList);
    }
}
