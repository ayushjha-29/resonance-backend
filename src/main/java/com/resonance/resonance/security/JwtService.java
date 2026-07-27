package com.resonance.resonance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username){

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token){

        return extractAllClaims(token).getSubject();

    }

    public Date extractExpiration(String token){

        return extractAllClaims(token).getExpiration();

    }

    public boolean isTokenValid(String token , UserDetails userDetails){

        String username = userDetails.getUsername();

        return extractUsername(token).equals(username) && !isTokenExpired(token);

    }

    private SecretKey getSigningKey(){

        byte [] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    private Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());

    }

}
