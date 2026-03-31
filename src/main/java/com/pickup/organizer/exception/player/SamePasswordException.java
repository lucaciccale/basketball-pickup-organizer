package com.pickup.organizer.exception.player;

public class SamePasswordException extends RuntimeException {

    public SamePasswordException() {
        super("New password cannot be the same as the old password.");
    }

}
