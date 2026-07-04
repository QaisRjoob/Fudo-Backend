package com.fudo.shared.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, String[]> errors;

    public ValidationException(Map<String, String[]> errors) {
        super("One or more validation errors occurred.");
        this.errors = errors;
    }

    public ValidationException(String field, String message) {
        super(message);
        this.errors = new HashMap<>();
        this.errors.put(field, new String[] { message });
    }

    public Map<String, String[]> getErrors() {
        return errors;
    }
}
