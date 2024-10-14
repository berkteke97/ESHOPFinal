package com.example.teke.ESHOP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerExistsException extends RuntimeException {
     public CustomerExistsException(String message) {
         super(message);
     }
}
