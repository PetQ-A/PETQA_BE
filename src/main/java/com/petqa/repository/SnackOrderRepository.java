package com.petqa.repository;

import com.petqa.domain.SnackOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnackOrderRepository extends JpaRepository<SnackOrder, Long> {
    List<SnackOrder> findByUserId(Long userId);
}
