package com.example.onlinestore.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(Class clazz, Long id) {
        super(String.format("%s with id %d not found", clazz.getSimpleName(), id));
    }
}
