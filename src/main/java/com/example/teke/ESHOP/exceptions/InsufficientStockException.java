package com.example.teke.ESHOP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InsufficientStockException extends  Exception{
    public InsufficientStockException(String msg) {
     super(msg);
    }
}
