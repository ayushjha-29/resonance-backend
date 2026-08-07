package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.ForgetPasswordRequest;
import com.resonance.resonance.dto.request.LoginRequest;
import com.resonance.resonance.dto.request.RegisterRequest;
import com.resonance.resonance.dto.request.ResetPasswordRequest;
import com.resonance.resonance.dto.response.LoginResponse;
import com.resonance.resonance.dto.response.RegisterResponse;
import com.resonance.resonance.entity.AppUser;
import com.resonance.resonance.entity.Token;
import com.resonance.resonance.enums.Role;
import com.resonance.resonance.exception.DuplicateResourceException;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.exception.TokenExpiredException;
import com.resonance.resonance.exception.TokenNotValidException;
import com.resonance.resonance.mapper.AppUserMapper;
import com.resonance.resonance.repository.AppUserRepository;
import com.resonance.resonance.repository.TokenRepository;
import com.resonance.resonance.security.JwtService;
import com.resonance.resonance.utils.ValidationUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    public RegisterResponse userRegister(RegisterRequest request) throws MessagingException {

        return register(request , Role.USER);

    }

    public RegisterResponse artistRegister(RegisterRequest request) throws MessagingException {

        return register(request , Role.ARTIST);

    }

    @Transactional
    public String verifyEmail(String token) throws MessagingException {

        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(() -> new TokenNotValidException());

        if(verificationToken.getExpiration().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException();
        }

        AppUser appUser = verificationToken.getAppUser();

        appUser.setEnabled(true);
        appUser.setToken(null);

        tokenRepository.delete(verificationToken);

        appUserRepository.save(appUser);

        emailService.sendWelcomeEmail(appUser);

        return """
                    Your email has been verified successfully!

                    Welcome to Resonance.

                    Your account is now active. You can now log in and start exploring your favorite music.
                """;

    }

    public String forgotPassword(ForgetPasswordRequest request) throws MessagingException {

        String email = request.getEmail();

        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Token token = createToken(appUser,1);

        appUser.setToken(token);

        appUserRepository.save(appUser);

        String resetLink = "http://192.168.29.29:8080/auth/reset-password?token="+token.getToken();

        emailService.sendResetPasswordEmail(appUser,resetLink);

        return "An email to reset the password has been sent to "+request.getEmail()+".";

    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request , String token){

        Token passwordResetToken = tokenRepository.findByToken(token).orElseThrow(() -> new TokenNotValidException());

        if(passwordResetToken.getExpiration().isBefore(LocalDateTime.now())){

            throw new TokenExpiredException();

        }

        AppUser user = passwordResetToken.getAppUser();

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encodedPassword);
        user.setToken(null);

        tokenRepository.delete(passwordResetToken);

        appUserRepository.save(user);

        return "Your password has been reset successfully.";

    }

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getIdentifier() , request.getPassword()));

        AppUser appUser;

        if(ValidationUtils.isEmail(request.getIdentifier())){
            appUser = appUserRepository.findByEmail(request.getIdentifier()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        }
        else {
            appUser = appUserRepository.findByUsername(request.getIdentifier()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        }

        String token = jwtService.generateToken(appUser.getUsername());

        Date expiration = jwtService.extractExpiration(token);

        return entityToResponse(appUser,token,expiration);

    }

    private RegisterResponse register(RegisterRequest request , Role role) throws MessagingException {

        if(appUserRepository.existsByUsername(request.getUsername())){
            throw new DuplicateResourceException("Username already exists.");
        }

        if(appUserRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already exists.");
        }

        AppUser appUser = appUserMapper.toEntity(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        appUser.setPassword(encodedPassword);
        appUser.setRole(role);

        Token token = createToken(appUser,24);

        appUser.setToken(token);

        appUserRepository.save(appUser);

        String verificationLink = "http://192.168.29.29:8080/auth/verify?token="+token.getToken();

        emailService.sendVerificationEmail(appUser,verificationLink);

        return appUserMapper.toDTO(appUser);

    }

    private LoginResponse entityToResponse(AppUser appUser , String token , Date expiration){

       return new LoginResponse(appUser.getUsername() , token , expiration);

    }

    private Token createToken(AppUser appUser , int hours){

        return Token.builder()
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusHours(hours))
                .appUser(appUser)
                .build();

    }

}
