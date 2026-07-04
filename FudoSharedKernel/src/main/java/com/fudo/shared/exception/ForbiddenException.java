package com.fudo.shared.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        this("Access denied.");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
