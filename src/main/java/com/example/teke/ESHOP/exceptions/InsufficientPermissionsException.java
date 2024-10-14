package com.example.teke.ESHOP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String message){
        super(message);
    }
}
