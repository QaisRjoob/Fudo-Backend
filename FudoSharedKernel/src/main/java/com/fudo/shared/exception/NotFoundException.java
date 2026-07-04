package com.fudo.shared.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String resource, Object key) {
        super("%s with key '%s' was not found.".formatted(resource, key));
    }
}
