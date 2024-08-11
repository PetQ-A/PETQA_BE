package com.petqa.service.snack;

import com.petqa.converter.SnackConverter;
import com.petqa.domain.Snack;
import com.petqa.domain.SnackOrder;
import com.petqa.domain.User;
import com.petqa.dto.snack.SnackOrderDTO;
import com.petqa.dto.snack.SnackOrderListDTO;
import com.petqa.dto.snack.SnackResponseDTO;
import com.petqa.repository.SnackOrderRepository;
import com.petqa.repository.SnackRepository;
import com.petqa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.petqa.converter.SnackConverter.toSnackOrderListDTO;

@Transactional
@Service
@RequiredArgsConstructor
public class SnackServiceImpl implements SnackService{
    private final SnackRepository snackRepository;
    private final SnackOrderRepository snackOrderRepository;
    private final UserRepository userRepository;

    @Override
    public List<SnackResponseDTO.SnackListDTO> getAllSnacks() {
        List<SnackResponseDTO.SnackListDTO> snacks = snackRepository.findAll().stream()
                .map(snack -> {
                    return SnackConverter.toSnackListDTO(snack);
                })
                .collect(Collectors.toList());
        return snacks;
    }

    @Override
    public void OrderSnack(SnackOrderDTO.SnackOderRequestDTO request){

        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new IllegalArgumentException("Member not found"));
        Snack snack=snackRepository.findById(request.getSnackId())
                .orElseThrow(()->new IllegalArgumentException("Snack not found"));
        if (user.getPoints() == 1000) {
            // 멤버의 포인트가 1000일 때
            user.setPoints(0);
            userRepository.save(user);
        } else {
            // 멤버의 포인트가 1000이 아닐 때 예외 발생
            throw new IllegalArgumentException("구매할 수 없습니다. 필요한 포인트: 1000");
        }
        SnackOrder snackOrder=SnackConverter.toSnackOrder(user,snack);

        snackOrderRepository.save(snackOrder);

    }

    @Override
    public List<SnackOrderListDTO.SnackOrderListResponseDTO> getOrderList(Long userId) {
        List<SnackOrder> orders = snackOrderRepository.findByUserId(userId);
        return orders.stream()
                .map(SnackConverter-> {
                    return toSnackOrderListDTO(SnackConverter);
                })
                .collect(Collectors.toList());
    }
}
