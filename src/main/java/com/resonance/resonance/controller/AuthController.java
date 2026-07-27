package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.LoginRequest;
import com.resonance.resonance.dto.request.RegisterRequest;
import com.resonance.resonance.dto.response.LoginResponse;
import com.resonance.resonance.dto.response.RegisterResponse;
import com.resonance.resonance.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponse> userRegister(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.userRegister(request));

    }

    @PostMapping("/register/artist")
    public ResponseEntity<RegisterResponse> artistRegister(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.artistRegister(request));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){

        return ResponseEntity.ok(authenticationService.login(request));

    }
}
