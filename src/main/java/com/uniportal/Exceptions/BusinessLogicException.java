package com.uniportal.Exceptions;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends UniPortalException {
    public BusinessLogicException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}