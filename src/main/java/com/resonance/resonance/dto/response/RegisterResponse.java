package com.resonance.resonance.dto.response;

import com.resonance.resonance.enums.Role;
import lombok.Data;

@Data
public class RegisterResponse {

    private String id;
    private String username;
    private Role role;

}
