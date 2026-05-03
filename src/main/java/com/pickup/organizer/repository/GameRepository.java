package com.pickup.organizer.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    @Query(
        "SELECT COUNT(g) > 0 FROM Game g "
        + "WHERE g.location = :location "
        + "AND g.status IN :statuses "  
        + "AND g.dateTime > :startMinusGameDuration "
        + "AND g.dateTime < :startPlusGameDuration "
        + "AND (:excludeId IS NULL OR :excludeId <> g.id)"
    )
    boolean existsOverlappingGameAtLocation(
        String location,
        List<GameStatus> statuses,
        LocalDateTime startMinusGameDuration,
        LocalDateTime startPlusGameDuration,
        Long excludeId
    );

}
