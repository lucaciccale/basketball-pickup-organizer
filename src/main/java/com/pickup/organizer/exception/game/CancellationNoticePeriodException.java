package com.pickup.organizer.exception.game;

import static com.pickup.organizer.service.GameService.MIN_TIME_IN_ADVANCE_HRS;;

public class CancellationNoticePeriodException extends RuntimeException {

    public CancellationNoticePeriodException() {
        super("Game must be cancelled at least " + MIN_TIME_IN_ADVANCE_HRS + " hours in advance.");
    }

}
