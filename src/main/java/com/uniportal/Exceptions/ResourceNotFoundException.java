package com.uniportal.Exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends UniPortalException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}