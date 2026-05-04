package com.pickup.organizer.exception.game;

public class InvalidCapacityException extends RuntimeException {

    public InvalidCapacityException(Integer capacity, Integer currentParticipants) {
        super("Cannot reduce game capacity to '" + capacity + "'."
            + " There are already '" + currentParticipants + "' players registered.");
    }
}
