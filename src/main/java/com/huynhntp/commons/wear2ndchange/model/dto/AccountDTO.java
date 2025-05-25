package com.huynhntp.commons.wear2ndchange.model.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String username;
    private String password;
    private String role;
}
