package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
