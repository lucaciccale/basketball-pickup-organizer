package com.pickup.organizer.exception.game;

import com.pickup.organizer.enums.GameStatus;

public class GameCancellationException extends RuntimeException {

    public GameCancellationException(String message) {
        super(message);
    }

    public GameCancellationException(GameStatus status) {
        this(getMessageByStatus(status));
    }

    private static String getMessageByStatus(GameStatus status) {
        switch (status) {
            case COMPLETED:
                return "Cannot cancel a game that has already finished.";
            case IN_PROGRESS:
                return "Cannot cancel a game that is already in progress.";
            case CANCELLED:
                return "Cannot cancel a game that has already been cancelled.";
            default:
                return "Unknown error.";
        }
    }

}
