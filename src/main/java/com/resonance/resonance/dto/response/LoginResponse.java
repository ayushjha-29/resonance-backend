package com.resonance.resonance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String username;
    private String jwt;
    private Date expiration;

}
