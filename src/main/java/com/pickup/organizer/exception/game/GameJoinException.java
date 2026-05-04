package com.pickup.organizer.exception.game;

import com.pickup.organizer.enums.GameStatus;

public class GameJoinException extends RuntimeException {

    public GameJoinException(String message) {
        super(message);
    }

    public GameJoinException(GameStatus status) {
        this(getMessageByStatus(status));
    }

    private static String getMessageByStatus(GameStatus status) {
        switch (status) {
            case COMPLETED:
                return "Cannot join a game that has already finished.";
            case FULL:
                return "Cannot join. Game is already full!";
            case IN_PROGRESS:
                return "Cannot join a game that is already in progress.";
            case CANCELLED:
                return "Cannot join a game that has already been cancelled.";
            default:
                return "Unknown error.";
        }
    }

}
