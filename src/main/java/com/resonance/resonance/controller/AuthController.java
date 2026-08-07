package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.ForgetPasswordRequest;
import com.resonance.resonance.dto.request.LoginRequest;
import com.resonance.resonance.dto.request.RegisterRequest;
import com.resonance.resonance.dto.request.ResetPasswordRequest;
import com.resonance.resonance.dto.response.LoginResponse;
import com.resonance.resonance.dto.response.RegisterResponse;
import com.resonance.resonance.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponse> userRegister(@Valid @RequestBody RegisterRequest request) throws MessagingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.userRegister(request));

    }

    @PostMapping("/register/artist")
    public ResponseEntity<RegisterResponse> artistRegister(@Valid @RequestBody RegisterRequest request) throws MessagingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.artistRegister(request));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){

        return ResponseEntity.ok(authenticationService.login(request));

    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) throws MessagingException {

        return ResponseEntity.ok(authenticationService.verifyEmail(token));

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request) throws MessagingException {

        return ResponseEntity.ok(authenticationService.forgotPassword(request));

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request , @RequestParam String token){

        return ResponseEntity.ok(authenticationService.resetPassword(request,token));

    }

}
