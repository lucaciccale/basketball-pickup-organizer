package com.pickup.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pickup.organizer.entity.GameParticipant;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    @Query(
        "SELECT COUNT(gp) "
        + "FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId"
    )
    Long countByGameId(Long gameId);

}
