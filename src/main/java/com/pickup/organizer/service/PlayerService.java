package com.pickup.organizer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.DuplicateEmailException;
import com.pickup.organizer.repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Player registerPlayer(Player newPlayer) {
        String email = newPlayer.getEmail();
        if (repository.findByEmail(email) == null) {
            repository.save(newPlayer);
            return newPlayer;
        } else {
            throw new DuplicateEmailException(email);
        }
    }

    public Optional<Player> findById(Long id) {
        return repository.findById(id);
    }
}
