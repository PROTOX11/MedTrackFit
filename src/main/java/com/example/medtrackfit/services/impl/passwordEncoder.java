package com.example.medtrackfit.services.impl;

/**
 * Simple placeholder implementation to avoid throwing UnsupportedOperationException
 * (real password encoding is provided by Spring's PasswordEncoder bean in SecurityConfig).
 */
public class passwordEncoder {

    public String encode(String input) {
        // Not used for authentication; keep as passthrough to avoid exceptions if referenced.
        return input == null ? null : input;
    }

}
