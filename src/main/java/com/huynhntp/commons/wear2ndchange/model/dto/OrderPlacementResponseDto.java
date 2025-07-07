package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacementResponseDto {
    private Long orderId;
    private String message;
    private Double totalAmount;
    private String status;
} 