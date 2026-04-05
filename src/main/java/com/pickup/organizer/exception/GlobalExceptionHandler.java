package com.pickup.organizer.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import com.pickup.organizer.exception.player.*;
import com.pickup.organizer.exception.game.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, String path) {
        ApiError error = new ApiError(
            status.value(),
            status.getReasonPhrase(),
            message,
            path
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> messages = 
            ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .toList();
        String message = String.join("; ", messages);
        return buildError(
            HttpStatus.BAD_REQUEST,
            message,
            request.getRequestURI()
        );
    }

    // TODO: handle invaid birth date format exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidFormatExceptions(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Malformed JSON request.";
        return buildError(
            HttpStatus.BAD_REQUEST,
            message,
            request.getRequestURI()
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiError> handleDuplicateEmailException(DuplicateEmailException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiError> handlePlayerNotFoundException(PlayerNotFoundException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiError> hanldeIncorrectPasswordException(IncorrectPasswordException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ApiError> handleSamePasswordException(SamePasswordException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ApiError> handleGameNotFoundException(GameNotFoundException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidGameTimeException.class)
    public ResponseEntity<ApiError> handleInvalidGameTimeException(InvalidGameTimeException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(OverlappingGameException.class)
    public ResponseEntity<ApiError> handleDuplicateLocationException(OverlappingGameException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiError> handleInvalidDateRangeException(InvalidDateRangeException ex, HttpServletRequest request) {
        return buildError(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

}
