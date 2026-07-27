package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Name cannot be blank.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

}
