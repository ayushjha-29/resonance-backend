package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.LoginRequest;
import com.resonance.resonance.dto.request.RegisterRequest;
import com.resonance.resonance.dto.response.LoginResponse;
import com.resonance.resonance.dto.response.RegisterResponse;
import com.resonance.resonance.entity.AppUser;
import com.resonance.resonance.enums.Role;
import com.resonance.resonance.exception.DuplicateUserException;
import com.resonance.resonance.mapper.AppUserMapper;
import com.resonance.resonance.repository.AppUserRepository;
import com.resonance.resonance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse userRegister(RegisterRequest request){

        return register(request , Role.USER);

    }

    public RegisterResponse artistRegister(RegisterRequest request){

        return register(request , Role.ARTIST);

    }

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername() , request.getPassword()));

        AppUser appUser = appUserRepository.findByUsername(request.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found."));

        String token = jwtService.generateToken(appUser.getUsername());

        Date expiration = jwtService.extractExpiration(token);

        return entityToResponse(appUser,token,expiration);

    }

    private RegisterResponse register(RegisterRequest request , Role role){

        if(appUserRepository.existsByUsername(request.getUsername())){
            throw new DuplicateUserException();
        }

        AppUser appUser = appUserMapper.toEntity(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        appUser.setPassword(encodedPassword);
        appUser.setRole(role);

        appUserRepository.save(appUser);

        return appUserMapper.toDTO(appUser);

    }

    private LoginResponse entityToResponse(AppUser appUser , String token , Date expiration){

       return new LoginResponse(appUser.getUsername() , token , expiration);

    }

}
