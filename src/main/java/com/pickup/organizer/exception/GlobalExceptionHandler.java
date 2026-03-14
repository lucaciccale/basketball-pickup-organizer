package com.pickup.organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        String errorMsg = ex.getMessage();
        System.out.println(errorMsg);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);
    }
}

