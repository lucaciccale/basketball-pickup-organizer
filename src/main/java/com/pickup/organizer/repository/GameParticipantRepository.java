package com.pickup.organizer.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pickup.organizer.entity.GameParticipant;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    @Query(
        "SELECT COUNT(gp) > 0 "
        + "FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId "
        + "AND gp.player.id = :playerId"
    )
    boolean existsPlayerAtGame(Long playerId, Long gameId);

    @Query(
        "SELECT COUNT(gp) > 0 "
        + "FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId "
    )
    boolean existsAnyPlayerAtGame(Long gameId);

    @Query(
        "SELECT gp "
        + "FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId "
        + "AND gp.player.id = :playerId"
    )
    Optional<GameParticipant> findPlayerAtGame(Long gameId, Long playerId);

    @Query(
        "SELECT gp "
        + "FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId"
    )
    Page<GameParticipant> searchPlayersAtGame(Long gameId, Pageable pageable);

    @Modifying
    @Query(
        "DELETE FROM GameParticipant gp "
        + "WHERE gp.game.id = :gameId "
        + "AND gp.player.id = :playerId"
    )
    void deletePlayerAtGame(Long gameId, Long playerId);

}
