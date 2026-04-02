package com.pickup.organizer.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.pickup.organizer.dto.game.GameCreateDto;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;
import com.pickup.organizer.exception.game.*;
import com.pickup.organizer.repository.GameRepository;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository repository;
    private static final int GAME_DURATION_HRS = 2;
    private static final int MIN_TIME_IN_ADVANCE_HRS = 1;
    private static final int MAX_TIME_IN_ADVANCE_DAYS = 30;

    public Game findGameById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

    @Transactional
    public Game createGame(GameCreateDto newGame) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_TIME_IN_ADVANCE_HRS);
        if (newGame.getDateTime().isBefore(minAllowedTime)) {
            throw new InvalidGameTimeException("Game must be scheduled at least 1 hour in advance.");
        }

        LocalDateTime maxAllowedTime = LocalDateTime.now().plusDays(MAX_TIME_IN_ADVANCE_DAYS);
        if (newGame.getDateTime().isAfter(maxAllowedTime)) {
            throw new InvalidGameTimeException("Game cannot be scheduled more than 30 days in advance.");
        }

        String location = newGame.getLocation()
            .trim()
            .toUpperCase()
            .replaceAll("\\s+", " ");
        LocalDateTime startMinusGameDuration = newGame.getDateTime().minusHours(GAME_DURATION_HRS);
        LocalDateTime startPlusGameDuration = newGame.getDateTime().plusHours(GAME_DURATION_HRS);
        if (repository.existsOverlappingGameAtLocation(
            location,
            GameStatus.OPEN,
            startMinusGameDuration,
            startPlusGameDuration
        )) {
            throw new OverlappingGameException(location);
        }

        Game game = Game.builder()
            .location(location)
            .dateTime(newGame.getDateTime())
            .maxPlayers(newGame.getMaxPlayers())
            .status(GameStatus.OPEN)
            .build();
        return repository.save(game);
    }

}
