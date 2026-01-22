package com.uniportal.Exceptions;

import org.springframework.http.HttpStatus;

public abstract class UniPortalException extends RuntimeException{
    private final HttpStatus status;

    public UniPortalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
