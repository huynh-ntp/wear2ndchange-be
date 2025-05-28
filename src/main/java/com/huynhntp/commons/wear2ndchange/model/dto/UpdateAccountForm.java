package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

@Data
public class UpdateAccountForm {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String avatarUrl;
    private String address;
}
