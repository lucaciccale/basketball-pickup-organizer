package com.pickup.organizer.exception.game;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Game with id " + id + " not found.");
    }

}
