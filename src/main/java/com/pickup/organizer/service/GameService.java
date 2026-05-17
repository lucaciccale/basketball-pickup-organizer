package com.pickup.organizer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.pickup.organizer.dto.game.*;
import com.pickup.organizer.entity.*;
import com.pickup.organizer.enums.GameStatus;
import com.pickup.organizer.exception.game.*;
import com.pickup.organizer.exception.game.participant.*;
import com.pickup.organizer.repository.GameParticipantRepository;
import com.pickup.organizer.repository.GameRepository;
import com.pickup.organizer.specification.GameSpecifications;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository repository;
    private final GameParticipantRepository participantRepository;
    private final PlayerService playerService;

    public static final List<GameStatus> TERMINAL_STATUSES = List.of(
        GameStatus.COMPLETED,
        GameStatus.IN_PROGRESS,
        GameStatus.CANCELLED
    );
    private static final List<GameStatus> VALID_STATUSES = List.of(
        GameStatus.OPEN,
        GameStatus.FULL,
        GameStatus.IN_PROGRESS
    );

    public static final int GAME_DURATION_HRS = 2;
    public static final int MIN_MINS_IN_ADVANCE = 30;
    public static final int MIN_HRS_IN_ADVANCE = 1;
    public static final int MIN_DAYS_IN_ADVANCE = 2;
    public static final int MAX_DAYS_IN_ADVANCE = 30;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    @Transactional
    public void updateTemporalStatuses() {
        LocalDateTime nowMinusGameDuration = LocalDateTime.now().minusHours(GAME_DURATION_HRS);
        repository.updateInProgressStatus(LocalDateTime.now(), nowMinusGameDuration);
        repository.updateCompletedStatus(LocalDateTime.now(), nowMinusGameDuration);
    }
    
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
    public Game joinGame(Long id, JoinGameDto dto) {
        Game game = findGameById(id);
        Player player = playerService.findPlayerById(dto.getPlayerId());
        validateJoinable(game, dto);
        GameParticipant participant = GameParticipant.builder()
            .player(player)
            .game(game)
            .skillRating(dto.getSkillRating())
            .build();
        participantRepository.save(participant);
        game.setCurrentPlayers(game.getCurrentPlayers() + 1);
        refreshGameStatus(game);
        return game;
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

    public GameParticipant findParticipant(Long gameId, Long playerId) {
        checkGameExistence(gameId);
        playerService.checkPlayerExistence(playerId);
        return participantRepository.findPlayerAtGame(gameId, playerId)
            .orElseThrow(() -> new GameParticipantNotFoundException(gameId, playerId));
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
        refreshGameStatus(game);
        return repository.save(game);
    }

    @Transactional
    public Game leaveGame(Long gameId, Long playerId) {
        Game game = findGameById(gameId);
        playerService.checkPlayerExistence(playerId);
        validateAbleToLeave(game, playerId);
        participantRepository.deletePlayerAtGame(gameId, playerId);
        game.setCurrentPlayers(game.getCurrentPlayers() - 1);
        refreshGameStatus(game);
        return game;
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
        if (repository.existsOverlappingGameAtLocation(
                location,
                VALID_STATUSES,
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

    private void validateJoinable(Game game, JoinGameDto dto) {
        GameStatus status = game.getStatus();
        if (status != GameStatus.OPEN) {
            throw new GameJoinException(status);
        }
        if (participantRepository.existsPlayerAtGame(dto.getPlayerId(), game.getId())) {
            throw new GameJoinException("Cannot join. Player is already part of this game.");
        }
        LocalDateTime minAllowedTime = LocalDateTime.now().plusMinutes(MIN_MINS_IN_ADVANCE);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new GameJoinException("Cannot join a game less than '" + MIN_MINS_IN_ADVANCE + "' minutes in advance.");
        }
    }

    private void validateCancelable(Game game) {
        if (TERMINAL_STATUSES.contains(game.getStatus())) {
            throw new GameCancellationException(game.getStatus());
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
        if (TERMINAL_STATUSES.contains(game.getStatus())) {
            throw new GameUpdateException(game.getStatus());
        }
    }

    private void validateCapacityUpdate(Game game, GameUpdateDto dto) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new GameUpdateException("Game capacity must be updated at least '" + MIN_HRS_IN_ADVANCE + "' hour/s in advance.");
        }
        if (dto.getMaxPlayers() < game.getCurrentPlayers()) {
            throw new InvalidCapacityException(dto.getMaxPlayers(), game.getCurrentPlayers());
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
        LocalDateTime dateTime = dto.getDateTime() != null 
            ? dto.getDateTime()
            : game.getDateTime();
        if (dateTime.isBefore(minAllowedTime)) {
            throw new GameUpdateException("Game location must be updated at least '" + MIN_DAYS_IN_ADVANCE + "' days in advance.");
        }
        ensureNoOverlappingGames(normalizeLocation(dto.getLocation()), dateTime, game.getId());
    }

    private void validateAbleToLeave(Game game, Long playerId) {
        if (TERMINAL_STATUSES.contains(game.getStatus())) {
            throw new GameLeaveException(game.getStatus());
        }
        if (!participantRepository.existsPlayerAtGame(playerId, game.getId())) {
            throw new GameLeaveException("Cannot leave a game that your're not participating in.");
        }
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        if (game.getDateTime().isBefore(minAllowedTime)) {
            throw new GameLeaveException("Cannot leave the game less than '" + MIN_HRS_IN_ADVANCE + "' hour/s in advance.");
        }
    }

    private void refreshGameStatus(Game game) {
        if (game.getCurrentPlayers() < game.getMaxPlayers()) {
            game.setStatus(GameStatus.OPEN);
        }
        else if (game.getCurrentPlayers() == game.getMaxPlayers()) {
            game.setStatus(GameStatus.FULL);
        }
    }

    public void checkGameExistence(Long id) {
        if (!repository.existsById(id)) {
            throw new GameNotFoundException(id);
        }
    }

}
