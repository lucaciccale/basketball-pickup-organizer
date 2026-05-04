package com.pickup.organizer.exception.game;

import com.pickup.organizer.enums.GameStatus;

public class GameUpdateException extends RuntimeException {

    public GameUpdateException(String message) {
        super(message);
    }

    public GameUpdateException(GameStatus status) {
        this(getMessageByStatus(status));
    }

    private static String getMessageByStatus(GameStatus status) {
        switch (status) {
            case COMPLETED:
                return "Cannot update a game that has already finished.";
            case IN_PROGRESS:
                return "Cannot update a game that is already in progress.";
            case CANCELLED:
                return "Cannot update a game that has already been cancelled.";
            default:
                return "Unknown error.";
        }
    }

}
