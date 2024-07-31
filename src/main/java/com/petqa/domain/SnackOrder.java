package com.petqa.domain;

import com.petqa.domain.common.BaseEntity;
import com.petqa.domain.enums.SnackOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnackOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_id")
    private Snack snack;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SnackOrderStatus status;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;
}
