package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordRequest {

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

}
