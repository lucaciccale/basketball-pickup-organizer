package com.pickup.organizer.exception.player;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email '" + email + "' is already taken!");
    }

}
