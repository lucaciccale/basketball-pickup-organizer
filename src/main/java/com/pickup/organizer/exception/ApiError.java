package com.pickup.organizer.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ApiError {

    private final LocalDateTime timestamp;
    private final int statusCode;
    private final String errorName;
    private final String message;
    private final String path;

    public ApiError(int statusCode, String errorName, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.errorName = errorName;
        this.message = message;
        this.path = path;
    }
}
