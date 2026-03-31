package com.pickup.organizer.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.pickup.organizer.entity.Game;
import com.pickup.organizer.exception.game.*;
import com.pickup.organizer.repository.GameRepository;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository repository;

    public Game findGameById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

}
