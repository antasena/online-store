package com.example.onlinestore.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessValidationError extends RuntimeException {
    public BusinessValidationError(String message) {
        super(message);
    }

    public BusinessValidationError(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessValidationError(Throwable cause) {
        super(cause);
    }
}
