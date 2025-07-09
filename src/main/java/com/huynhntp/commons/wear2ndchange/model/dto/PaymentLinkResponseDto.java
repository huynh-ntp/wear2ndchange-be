package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponseDto {
    private String checkoutUrl;
    private String orderCode;
    private Long orderId;
    private Double amount;
    private String status;
} 