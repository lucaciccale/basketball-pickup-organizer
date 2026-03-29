package com.pickup.organizer.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("The old password provided is incorrect.");
    } 
}
