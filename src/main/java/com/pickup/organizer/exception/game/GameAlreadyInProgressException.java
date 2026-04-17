package com.pickup.organizer.exception.game;

public class GameAlreadyInProgressException extends RuntimeException {

    public GameAlreadyInProgressException() {
        super("Cannot cancel a game that is already in progress.");
    }

}
