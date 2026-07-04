package com.fudo.shared.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        this("Unauthorized.");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
