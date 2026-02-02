package com.uniportal.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniPortalException.class)
    public ResponseEntity<ErrorMessage> handleUniPortalException(UniPortalException ex) {
    ErrorMessage error = new ErrorMessage(
            ex.getMessage(),
            Instant.now()
    );
    return new ResponseEntity<>(error, ex.getStatus());
}


    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
public ResponseEntity<ErrorMessage> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
    return new ResponseEntity<>(
            new ErrorMessage("Invalid email or password", Instant.now()),
            HttpStatus.UNAUTHORIZED
    );
}

@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
public ResponseEntity<ErrorMessage> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
    return new ResponseEntity<>(
            new ErrorMessage("You do not have permission to access this resource", Instant.now()),
            HttpStatus.FORBIDDEN
    );
}

}
