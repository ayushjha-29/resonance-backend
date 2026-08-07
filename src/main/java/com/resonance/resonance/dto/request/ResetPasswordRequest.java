package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Password cannot be blank.")
    private String newPassword;

}
