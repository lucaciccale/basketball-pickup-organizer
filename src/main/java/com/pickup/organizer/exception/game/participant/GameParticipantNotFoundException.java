package com.pickup.organizer.exception.game.participant;

public class GameParticipantNotFoundException extends RuntimeException {

    public GameParticipantNotFoundException(Long gameId, Long playerId) {
        super("Player with id: '" + playerId + "' not found at game with id: '" + gameId + "'.");
    }

}
