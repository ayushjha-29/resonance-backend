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
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void userRegister_shouldRegisterUser() throws MessagingException {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setEmail("test@gmail.com");
        request.setPassword("12345678");

        AppUser appUser = new AppUser();
        appUser.setUsername("test");
        appUser.setEmail("test@gmail.com");
        appUser.setPassword("12345678");

        String encodedPassword = "$12345678$";

        RegisterResponse response = new RegisterResponse();
        response.setUsername("test");
        response.setEmail("test@gmail.com");

        when(appUserRepository.existsByUsername("test")).thenReturn(false);
        when(appUserRepository.existsByEmail("test@gmail.com")).thenReturn(false);

        when(appUserMapper.toEntity(request)).thenReturn(appUser);

        when(passwordEncoder.encode("12345678")).thenReturn(encodedPassword);

        when(appUserRepository.save(appUser)).thenReturn(appUser);

        when(appUserMapper.toDTO(appUser)).thenReturn(response);

        RegisterResponse result = authenticationService.userRegister(request);

        assertEquals(request.getUsername(), result.getUsername());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(encodedPassword, appUser.getPassword());
        assertEquals(Role.USER, appUser.getRole());

        verify(appUserRepository).existsByUsername("test");
        verify(appUserRepository).existsByEmail("test@gmail.com");

        verify(appUserMapper).toEntity(request);
        verify(passwordEncoder).encode("12345678");
        verify(appUserRepository).save(appUser);
        verify(emailService).sendVerificationEmail(eq(appUser), anyString());
        verify(appUserMapper).toDTO(appUser);

    }

    @Test
    void artistRegister_shouldRegisterArtist() throws MessagingException {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setEmail("test@gmail.com");
        request.setPassword("12345678");

        AppUser appUser = new AppUser();
        appUser.setUsername("test");
        appUser.setEmail("test@gmail.com");
        appUser.setPassword("12345678");

        String encodedPassword = "$12345678$";

        RegisterResponse response = new RegisterResponse();
        response.setUsername("test");
        response.setEmail("test@gmail.com");

        when(appUserRepository.existsByUsername("test")).thenReturn(false);
        when(appUserRepository.existsByEmail("test@gmail.com")).thenReturn(false);

        when(appUserMapper.toEntity(request)).thenReturn(appUser);

        when(passwordEncoder.encode("12345678")).thenReturn(encodedPassword);

        when(appUserRepository.save(appUser)).thenReturn(appUser);

        when(appUserMapper.toDTO(appUser)).thenReturn(response);

        RegisterResponse result = authenticationService.artistRegister(request);

        assertEquals(request.getUsername(), result.getUsername());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(encodedPassword, appUser.getPassword());
        assertEquals(Role.ARTIST, appUser.getRole());

        verify(appUserRepository).existsByUsername("test");
        verify(appUserRepository).existsByEmail("test@gmail.com");

        verify(appUserMapper).toEntity(request);
        verify(passwordEncoder).encode("12345678");
        verify(appUserRepository).save(appUser);
        verify(emailService).sendVerificationEmail(eq(appUser), anyString());
        verify(appUserMapper).toDTO(appUser);

    }

    @Test
    void register_shouldThrowException_whenUsernameExists(){

        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");

        when(appUserRepository.existsByUsername("test")).thenReturn(true);

        assertThrows(DuplicateResourceException.class , () -> authenticationService.userRegister(request));

        verify(appUserRepository).existsByUsername("test");

    }

    @Test
    void register_shouldThrowException_whenEmailExists(){

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(appUserRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class , () -> authenticationService.userRegister(request));

        verify(appUserRepository).existsByEmail("test@gmail.com");

    }

    @Test
    void verifyEmail_shouldVerifyEmail() throws MessagingException {

        AppUser appUser = new AppUser();

        Token verificationToken = new Token();
        verificationToken.setAppUser(appUser);
        verificationToken.setExpiration(LocalDateTime.now().plusHours(1));

        when(tokenRepository.findByToken("token")).thenReturn(Optional.of(verificationToken));

        String result = authenticationService.verifyEmail("token");

        assertNotNull(result);
        assertTrue(appUser.getEnabled());
        assertNull(appUser.getToken());

        verify(tokenRepository).findByToken("token");
        verify(tokenRepository).delete(verificationToken);

        verify(appUserRepository).save(appUser);

        verify(emailService).sendWelcomeEmail(appUser);

    }

    @Test
    void verifyEmail_shouldThrowException_whenTokenNotFound() throws MessagingException {

        when(tokenRepository.findByToken("token")).thenReturn(Optional.empty());

        assertThrows(TokenNotValidException.class , () -> authenticationService.verifyEmail("token"));

        verify(tokenRepository).findByToken("token");

    }

    @Test
    void verifyEmail_shouldThrowException_whenTokenExpired() throws MessagingException {

        AppUser appUser = new AppUser();

        Token verificationToken = new Token();
        verificationToken.setAppUser(appUser);
        verificationToken.setExpiration(LocalDateTime.now().minusHours(1));

        when(tokenRepository.findByToken("token")).thenReturn(Optional.of(verificationToken));

        assertThrows(TokenExpiredException.class , () -> authenticationService.verifyEmail("token"));

        verify(tokenRepository).findByToken("token");

    }

    @Test
    void forgotPassword_shouldSendResetPasswordEmail() throws MessagingException {

        ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setEmail("test@gmail.com");

        AppUser appUser = new AppUser();
        appUser.setEmail("test@gmail.com");

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(appUser));

        String result = authenticationService.forgotPassword(request);

        assertEquals("An email to reset the password has been sent to test@gmail.com." , result);

        assertNotNull(appUser.getToken());

        verify(appUserRepository).findByEmail("test@gmail.com");
        verify(appUserRepository).save(appUser);

        verify(emailService).sendResetPasswordEmail(eq(appUser),anyString());

    }

    @Test
    void forgotPassword_shouldThrowException_whenUserNotFound(){

        ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setEmail("test@gmail.com");

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> authenticationService.forgotPassword(request));

        verify(appUserRepository).findByEmail("test@gmail.com");

    }

    @Test
    void resetPassword_shouldResetPassword(){

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("12345678");

        AppUser appUser = new AppUser();

        Token passwordResetToken = new Token();
        passwordResetToken.setExpiration(LocalDateTime.now().plusHours(1));
        passwordResetToken.setAppUser(appUser);

        when(tokenRepository.findByToken("token")).thenReturn(Optional.of(passwordResetToken));

        String encodedPassword = "$12345678$";

        when(passwordEncoder.encode("12345678")).thenReturn(encodedPassword);

        String result = authenticationService.resetPassword(request,"token");

        assertEquals("Your password has been reset successfully." , result);
        assertEquals(encodedPassword,appUser.getPassword());

        assertNull(appUser.getToken());

        verify(tokenRepository).findByToken("token");
        verify(tokenRepository).delete(passwordResetToken);

        verify(passwordEncoder).encode("12345678");

        verify(appUserRepository).save(appUser);

    }

    @Test
    void resetPassword_shouldThrowException_whenTokenNotFound(){

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("12345678");

        when(tokenRepository.findByToken("token")).thenReturn(Optional.empty());

        assertThrows(TokenNotValidException.class , () -> authenticationService.resetPassword(request,"token"));

        verify(tokenRepository).findByToken("token");

    }

    @Test
    void resetPassword_shouldThrowException_whenTokenExpired(){

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("12345678");

        AppUser appUser = new AppUser();

        Token passwordResetToken = new Token();
        passwordResetToken.setExpiration(LocalDateTime.now().minusHours(1));
        passwordResetToken.setAppUser(appUser);

        when(tokenRepository.findByToken("token")).thenReturn(Optional.of(passwordResetToken));

        assertThrows(TokenExpiredException.class , () -> authenticationService.resetPassword(request,"token"));

        verify(tokenRepository).findByToken("token");

    }

    @Test
    void login_shouldLoginUserWithEmail(){

        LoginRequest request = new LoginRequest();
        request.setIdentifier("test@gmail.com");
        request.setPassword("12345678");

        AppUser appUser = new AppUser();
        appUser.setUsername("test");

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(appUser));

        String jwt = "jwtToken";
        Date expiration = new Date(System.currentTimeMillis()+3600000);

        when(jwtService.generateToken("test")).thenReturn(jwt);
        when(jwtService.extractExpiration("jwtToken")).thenReturn(expiration);

        LoginResponse result = authenticationService.login(request);

        assertEquals(jwt,result.getJwt());
        assertEquals(expiration,result.getExpiration());
        assertEquals("test",result.getUsername());

        verify(appUserRepository).findByEmail("test@gmail.com");
        verify(appUserRepository,never()).findByUsername(anyString());

        verify(jwtService).generateToken("test");
        verify(jwtService).extractExpiration(jwt);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldLoginUserWithUsername(){

        LoginRequest request = new LoginRequest();
        request.setIdentifier("test");
        request.setPassword("12345678");

        AppUser appUser = new AppUser();
        appUser.setUsername("test");

        when(appUserRepository.findByUsername("test")).thenReturn(Optional.of(appUser));

        String jwt = "jwtToken";
        Date expiration = new Date(System.currentTimeMillis()+3600000);

        when(jwtService.generateToken("test")).thenReturn(jwt);
        when(jwtService.extractExpiration("jwtToken")).thenReturn(expiration);

        LoginResponse result = authenticationService.login(request);

        assertEquals(jwt,result.getJwt());
        assertEquals(expiration,result.getExpiration());
        assertEquals("test",result.getUsername());

        verify(appUserRepository).findByUsername("test");
        verify(appUserRepository,never()).findByEmail(anyString());

        verify(jwtService).generateToken("test");
        verify(jwtService).extractExpiration(jwt);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }

    @Test
    void login_shouldThrowException_whenEmailUserNotFound(){

        LoginRequest request = new LoginRequest();
        request.setIdentifier("test@gmail.com");
        request.setPassword("12345678");

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class , () -> authenticationService.login(request));

        verify(appUserRepository).findByEmail("test@gmail.com");
        verify(appUserRepository,never()).findByUsername(anyString());

    }

    @Test
    void login_shouldThrowException_whenUsernameUserNotFound(){

        LoginRequest request = new LoginRequest();
        request.setIdentifier("test");
        request.setPassword("12345678");

        when(appUserRepository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class , () -> authenticationService.login(request));

        verify(appUserRepository).findByUsername("test");
        verify(appUserRepository,never()).findByEmail(anyString());

    }

}
