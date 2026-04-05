package com.pickup.organizer.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;

public class GameSpecifications {

    public static Specification<Game> hasStatus(GameStatus status) {
        return (root, query, cb) ->
            status == null
            ? null
            : cb.equal(root.get("status"), status);
    }

    public static Specification<Game> isBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) ->
            (from == null || to == null)
            ? null
            : cb.between(root.get("dateTime"), from, to);
    }
    
}
