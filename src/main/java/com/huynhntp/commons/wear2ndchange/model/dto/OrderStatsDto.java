package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatsDto {
    private Double totalAmount;

    public OrderStatsDto(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
