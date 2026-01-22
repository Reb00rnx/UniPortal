package com.uniportal.Exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends UniPortalException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}