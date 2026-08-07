package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotBlank(message = "Email address cannot be blank.")
    @Email(message = "Please enter email.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

}
