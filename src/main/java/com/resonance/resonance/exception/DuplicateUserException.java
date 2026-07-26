package com.resonance.resonance.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("User already exists.");
    }
}
