package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private String status;
    private AccountDTO user;

    public Long getTotalPrice() {
        Long totalPrice = 0L;
        for (OrderItemDTO item : items) {
            totalPrice += item.getProduct().getPrice();
        }
        return totalPrice;
    }
}
