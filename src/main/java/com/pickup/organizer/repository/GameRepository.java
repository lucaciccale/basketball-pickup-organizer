package com.pickup.organizer.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(
        "SELECT COUNT(g) > 0 FROM Game g "
        + "WHERE g.location = :location "
        + "AND g.status = :status "  
        + "AND g.dateTime > :startMinusGameDuration "
        + "AND g.dateTime < :startPlusGameDuration"
    )
    boolean existsOverlappingGameAtLocation(
        String location,
        GameStatus status,
        LocalDateTime startMinusGameDuration,
        LocalDateTime startPlusGameDuration
    );

}
