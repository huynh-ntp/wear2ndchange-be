package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AccountDTO {
    private Long id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String role;
    private String avatarUrl;
    private Map<String, Object> preference;
}
