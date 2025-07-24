package com.example.businessservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessOwnerAlreadyExistsException extends RuntimeException {
    public BusinessOwnerAlreadyExistsException(String message) {
        super(message);
    }
}