package com.resonance.resonance.service;

import com.resonance.resonance.entity.AppUser;
import com.resonance.resonance.enums.Role;
import com.resonance.resonance.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadByUsername_shouldReturnUserDetails_whenEmailExists(){

        AppUser appUser = new AppUser();
        appUser.setUsername("test");
        appUser.setPassword("12345678");
        appUser.setRole(Role.USER);
        appUser.setEnabled(true);

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(appUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("test@gmail.com");

        assertEquals(appUser.getUsername(),result.getUsername());
        assertEquals(appUser.getPassword(),result.getPassword());
        assertTrue(result.getAuthorities().toString().contains("ROLE_USER"));
        assertTrue(result.isEnabled());

        verify(appUserRepository).findByEmail("test@gmail.com");
        verify(appUserRepository,never()).findByUsername(anyString());

    }

    @Test
    void loadByUsername_shouldReturnUserDetails_whenUsernameExists(){

        AppUser appUser = new AppUser();
        appUser.setUsername("test");
        appUser.setPassword("12345678");
        appUser.setRole(Role.USER);
        appUser.setEnabled(true);

        when(appUserRepository.findByUsername("test")).thenReturn(Optional.of(appUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("test");

        assertEquals(appUser.getUsername(),result.getUsername());
        assertEquals(appUser.getPassword(),result.getPassword());
        assertTrue(result.getAuthorities().toString().contains("ROLE_USER"));
        assertTrue(result.isEnabled());

        verify(appUserRepository).findByUsername("test");
        verify(appUserRepository , never()).findByEmail(anyString());

    }

    @Test
    void loadByUsername_shouldThrowException_whenEmailNotFound(){

        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,() -> customUserDetailsService.loadUserByUsername("test@gmail.com"));

        verify(appUserRepository).findByEmail("test@gmail.com");
        verify(appUserRepository,never()).findByUsername(anyString());

    }

    @Test
    void loadByUsername_shouldThrowException_whenUsernameNotFound(){

        when(appUserRepository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,() -> customUserDetailsService.loadUserByUsername("test"));

        verify(appUserRepository).findByUsername("test");
        verify(appUserRepository,never()).findByEmail(anyString());

    }

}
