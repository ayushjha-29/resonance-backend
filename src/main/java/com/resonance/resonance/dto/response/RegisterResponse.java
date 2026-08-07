package com.resonance.resonance.dto.response;

import com.resonance.resonance.enums.Role;
import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;

    private String message = "Registration successful. Please verify your email to activate your account.";
}
