package com.pickup.organizer.exception.game;

public class OverlappingGameException extends RuntimeException {

    public OverlappingGameException(String location) {
        super("An existing game at location '" + location + "' overlaps with the requested time.");
    }
    
}
