package com.resonance.resonance.config;

import com.resonance.resonance.enums.Role;
import com.resonance.resonance.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{

        return configuration.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/songs/**")
                        .hasAnyRole(Role.USER.name(), Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/songs/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/songs/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/songs/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/artists/**")
                        .hasAnyRole(Role.USER.name(), Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/artists/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/artists/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/artists/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/albums/**")
                        .hasAnyRole(Role.USER.name(), Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/albums/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/albums/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/albums/**")
                        .hasAnyRole(Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/genres/**")
                        .hasAnyRole(Role.USER.name(), Role.ARTIST.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/genres/**")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/genres/**")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/genres/**")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers("/swagger-ui/**" , "/swagger-ui.html" , "/v3/api-docs/**")
                        .permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }

}
