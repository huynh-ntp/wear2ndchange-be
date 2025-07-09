package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private List<Long> cartIds;
    private String receiver;
    private String phoneNumber;
    private String address;
    private String email;
    private String note;
}
