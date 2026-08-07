package com.resonance.resonance.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token expired.");
    }
}
