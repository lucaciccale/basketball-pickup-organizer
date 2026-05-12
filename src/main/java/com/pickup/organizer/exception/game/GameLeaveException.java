package com.pickup.organizer.exception.game;

import com.pickup.organizer.enums.GameStatus;

public class GameLeaveException extends RuntimeException {

    public GameLeaveException(String message) {
        super(message);
    }

    public GameLeaveException(GameStatus status) {
        this(getMessageByStatus(status));
    }

    private static String getMessageByStatus(GameStatus status) {
        switch (status) {
            case COMPLETED:
                return "Cannot leave a game that has already finished.";
            case IN_PROGRESS:
                return "Cannot leave a game that is already in progress.";
            case CANCELLED:
                return "Cannot leave a game that has already been cancelled.";
            default:
                return "Unknown error.";
        }
    }

}
