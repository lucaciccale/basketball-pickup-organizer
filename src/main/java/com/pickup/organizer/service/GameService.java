package com.pickup.organizer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.pickup.organizer.dto.game.*;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;
import com.pickup.organizer.exception.game.*;
import com.pickup.organizer.repository.GameParticipantRepository;
import com.pickup.organizer.repository.GameRepository;
import com.pickup.organizer.specification.GameSpecifications;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository repository;
    private final GameParticipantRepository participantRepository;

    public static final int GAME_DURATION_HRS = 2;
    public static final int MIN_HRS_IN_ADVANCE = 1;
    public static final int MIN_DAYS_IN_ADVANCE = 2;
    public static final int MAX_DAYS_IN_ADVANCE = 30;
    
    @Transactional
    public Game createGame(GameCreateDto newGame) {
        validateNewGame(newGame);
        Game game = Game.builder()
            .location(normalizeLocation(newGame.getLocation()))
            .dateTime(newGame.getDateTime())
            .maxPlayers(newGame.getMaxPlayers())
            .status(GameStatus.OPEN)
            .build();
        return repository.save(game);
    }
    
    @Transactional
    public Game cancelGame(Long id) {
        Game game = findGameById(id);
        validateCancelable(game);
        game.setStatus(GameStatus.CANCELLED);
        return repository.save(game);
    }
    
    public Game findGameById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

    public Page<Game> searchGames(GameStatus status, LocalDateTime from, LocalDateTime to, int page, int size) {
        validateDateRange(from, to);
        Specification<Game> spec = Specification
            .where(GameSpecifications.hasStatus(status))
            .and(GameSpecifications.isBetween(from, to));
        return repository.findAll(spec, PageRequest.of(page, size));
    }

    @Transactional
    public Game updateGame(Long id, GameUpdateDto dto) {
        Game game = findGameById(id);
        validateUpdatable(game);
        if (dto.getMaxPlayers() != null) {
            validateCapacityUpdate(game, dto);
            game.setMaxPlayers(dto.getMaxPlayers());
        }
        if (dto.getDateTime() != null && dto.getLocation() != null) {
            validateDateTimeUpdate(game, dto);
            validateLocationUpdate(game, dto);
            game.setDateTime(dto.getDateTime());
            game.setLocation(normalizeLocation(dto.getLocation()));
        }
        else if (dto.getLocation() != null) {
            validateLocationUpdate(game, dto);
            game.setLocation(normalizeLocation(dto.getLocation()));
        }
        else if (dto.getDateTime() != null) {
            validateDateTimeUpdate(game, dto);
            game.setDateTime(dto.getDateTime());
        }
        return repository.save(game);
    }

    @Transactional
    public void deleteGame(Long id) {
        repository.delete(findGameById(id));
    }
    
    private void validateNewGame(GameCreateDto newGame) {
        validateGameTime(newGame.getDateTime());
        ensureNoOverlappingGames(normalizeLocation(newGame.getLocation()), newGame.getDateTime(), null);
    }

    private void validateGameTime(LocalDateTime dateTime) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        if (dateTime.isBefore(minAllowedTime)) {
            throw new InvalidGameTimeException("Game must be scheduled at least '" + MIN_HRS_IN_ADVANCE + "' hour/s in advance.");
        }
        LocalDateTime maxAllowedTime = LocalDateTime.now().plusDays(MAX_DAYS_IN_ADVANCE);
        if (dateTime.isAfter(maxAllowedTime)) {
            throw new InvalidGameTimeException("Game cannot be scheduled more than '" + MAX_DAYS_IN_ADVANCE + "' days in advance.");
        }
    }

    private void ensureNoOverlappingGames(String location, LocalDateTime dateTime, Long excludeId) {
        LocalDateTime startMinusGameDuration = dateTime.minusHours(GAME_DURATION_HRS);
        LocalDateTime startPlusGameDuration = dateTime.plusHours(GAME_DURATION_HRS);
        List<GameStatus> statuses = List.of(
            GameStatus.OPEN,
            GameStatus.FULL,
            GameStatus.IN_PROGRESS
        );
        if (repository.existsOverlappingGameAtLocation(
                location,
                statuses,
                startMinusGameDuration,
                startPlusGameDuration,
                excludeId
        )) {
            throw new OverlappingGameException(location);
        }
    }

    private String normalizeLocation(String location) {
        return location
            .trim()
            .toUpperCase()
            .replaceAll("\\s+", " ");
    }

    private void validateCancelable(Game game) {
        if (game.getDateTime().isBefore(LocalDateTime.now())) {
            throw new GameCancellationException("Cannot cancel a game that has already finished.");
        }
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            throw new GameCancellationException("Cannot cancel a game that is already in progress.");
        }
        if (game.getStatus() == GameStatus.CANCELLED) {
            throw new GameCancellationException("Cannot cancel a game that is already cancelled.");
        }
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new GameCancellationException("Game must be cancelled at least '" + MIN_HRS_IN_ADVANCE + "' hour/s in advance.");
        }
    }

    private void validateDateRange(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new InvalidDateRangeException("Parameter 'to' must be after 'from'.");
        }
        if ((from == null) != (to == null)) {
            throw new InvalidDateRangeException("Parameters 'from' and 'to' must be provided together.");
        }
    }

    private void validateUpdatable(Game game) {
        if (game.getDateTime().isBefore(LocalDateTime.now())) {
            throw new GameUpdateException("Cannot update a game that has already finished.");
        }
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            throw new GameUpdateException("Cannot update a game that is already in progress.");
        }
        if (game.getStatus() == GameStatus.CANCELLED) {
            throw new GameUpdateException("Cannot update a game that is already cancelled.");
        }
    }

    private void validateCapacityUpdate(Game game, GameUpdateDto dto) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new GameUpdateException("Game capacity must be updated at least '" + MIN_HRS_IN_ADVANCE + "' hour/s in advance.");
        }
        Integer currentParticipants = participantRepository.countByGameId(game.getId());
        if (dto.getMaxPlayers() < currentParticipants) {
            throw new InvalidCapacityException(dto.getMaxPlayers(), currentParticipants);
        }
    }

    private void validateDateTimeUpdate(Game game, GameUpdateDto dto) {
        validateGameTime(dto.getDateTime());
        if (dto.getLocation() == null) {
            ensureNoOverlappingGames(normalizeLocation(game.getLocation()), dto.getDateTime(), game.getId());
        }
    }

    private void validateLocationUpdate(Game game, GameUpdateDto dto) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusDays(MIN_DAYS_IN_ADVANCE);
        LocalDateTime dateTime = dto.getDateTime() != null ? dto.getDateTime() : game.getDateTime();
        if (dateTime.isBefore(minAllowedTime)) {
            throw new GameUpdateException("Game location must be updated at least '" + MIN_DAYS_IN_ADVANCE + "' days in advance.");
        }
        ensureNoOverlappingGames(normalizeLocation(dto.getLocation()), dateTime, game.getId());
    }

}
