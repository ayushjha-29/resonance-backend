package com.resonance.resonance.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        super("Token not valid.");
    }
}
