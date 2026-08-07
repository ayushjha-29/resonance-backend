package com.resonance.resonance.utils;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isEmail(String identifier) {
        return EMAIL_PATTERN.matcher(identifier).matches();
    }
}
