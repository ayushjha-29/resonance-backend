package com.resonance.resonance.service;

import com.resonance.resonance.entity.AppUser;
import com.resonance.resonance.repository.AppUserRepository;
import com.resonance.resonance.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        AppUser appUser;

        if(ValidationUtils.isEmail(identifier)){
            appUser = appUserRepository.findByEmail(identifier).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        }
        else {
            appUser = appUserRepository.findByUsername(identifier).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        }

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole().name())
                .disabled(!appUser.getEnabled())
                .build();

    }
}
