package com.huynhntp.commons.wear2ndchange.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordForm {
    @NotBlank
    private String email;
}
