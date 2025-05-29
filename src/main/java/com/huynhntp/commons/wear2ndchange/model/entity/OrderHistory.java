package com.huynhntp.commons.wear2ndchange.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistory {
    public OrderHistory(Long orderId, String status, LocalDateTime createdAt, Long userId) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    @Id
    @GeneratedValue
    private Long id;

    private Long orderId;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Long userId;
}
