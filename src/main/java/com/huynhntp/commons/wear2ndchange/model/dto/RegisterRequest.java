package com.huynhntp.commons.wear2ndchange.model.dto;

import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;

    private String phoneNumber;

    @NotBlank
    private String role;
}