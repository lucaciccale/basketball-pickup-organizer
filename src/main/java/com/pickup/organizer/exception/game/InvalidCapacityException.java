package com.pickup.organizer.exception.game;

public class InvalidCapacityException extends RuntimeException {

    public InvalidCapacityException(Integer capacity, Long currentParticipants) {
        super("Cannot reduce game capacity to '" + capacity + "'."
            + " There are already '" + currentParticipants + "' players registered.");
    }
}
