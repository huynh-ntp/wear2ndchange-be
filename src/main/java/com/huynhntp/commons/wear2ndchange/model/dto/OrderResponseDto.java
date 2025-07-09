package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderResponseDto {

    private Long id;

    private Long userId;

    private Double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();

    private List<OrderItemDTO> items;

    private String status;

    private String receiver;
    private String phoneNumber;
    private String address;
    private String note;
    private String email;


}


