package com.pickup.organizer.exception.game;

public class PastGameCancellationException extends RuntimeException {

    public PastGameCancellationException() {
        super("Cannot cancel a game that has already finished.");
    }

}
