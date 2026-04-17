package com.pickup.organizer.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.pickup.organizer.dto.game.GameCreateDto;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;
import com.pickup.organizer.exception.game.*;
import com.pickup.organizer.repository.GameRepository;
import com.pickup.organizer.specification.GameSpecifications;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository repository;
    public static final int GAME_DURATION_HRS = 2;
    public static final int MIN_TIME_IN_ADVANCE_HRS = 1;
    public static final int MAX_TIME_IN_ADVANCE_DAYS = 30;

    private void validateDateRange(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new InvalidDateRangeException("Parameter 'to' must be after 'from'.");
        }
        if ((from == null) != (to == null)) {
            throw new InvalidDateRangeException("Parameters 'from' and 'to' must be provided together.");
        }
    }

    private void validateGameTime(LocalDateTime dateTime) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_TIME_IN_ADVANCE_HRS);
        if (dateTime.isBefore(minAllowedTime)) {
            throw new InvalidGameTimeException("Game must be scheduled at least 1 hour in advance.");
        }
        LocalDateTime maxAllowedTime = LocalDateTime.now().plusDays(MAX_TIME_IN_ADVANCE_DAYS);
        if (dateTime.isAfter(maxAllowedTime)) {
            throw new InvalidGameTimeException("Game cannot be scheduled more than 30 days in advance.");
        }
    }

    private void ensureNoOverlappingGames(String location, LocalDateTime dateTime) {
        LocalDateTime startMinusGameDuration = dateTime.minusHours(GAME_DURATION_HRS);
        LocalDateTime startPlusGameDuration = dateTime.plusHours(GAME_DURATION_HRS);
        if (repository.existsOverlappingGameAtLocation(
                location,
                GameStatus.OPEN,
                startMinusGameDuration,
                startPlusGameDuration
        )) {
            throw new OverlappingGameException(location);
        }
    }

    private void validateNewGame(String location, LocalDateTime dateTime) {
        validateGameTime(dateTime);
        ensureNoOverlappingGames(location, dateTime);
    }

    private String normalizeLocation(String location) {
        return location
            .trim()
            .toUpperCase()
            .replaceAll("\\s+", " ");
    }

    public Game findGameById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

    @Transactional
    public Game createGame(GameCreateDto newGame) {
        String location = normalizeLocation(newGame.getLocation());
        validateNewGame(location, newGame.getDateTime());
        Game game = Game.builder()
            .location(location)
            .dateTime(newGame.getDateTime())
            .maxPlayers(newGame.getMaxPlayers())
            .status(GameStatus.OPEN)
            .build();
        return repository.save(game);
    }

    public Page<Game> searchGames(GameStatus status, LocalDateTime from, LocalDateTime to, int page, int size) {
        validateDateRange(from, to);
        Specification<Game> spec = Specification
            .where(GameSpecifications.hasStatus(status))
            .and(GameSpecifications.isBetween(from, to));
        return repository.findAll(spec, PageRequest.of(page, size));
    }

    @Transactional
    public Game cancelGame(Long id) {
        Game game = findGameById(id);
        if (game.getDateTime().isBefore(LocalDateTime.now())) {
            throw new PastGameCancellationException();
        }
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_TIME_IN_ADVANCE_HRS);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new CancellationNoticePeriodException();
        }
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            throw new GameAlreadyInProgressException();
        }
        game.setStatus(GameStatus.CANCELLED);
        return repository.save(game);
    }

}
