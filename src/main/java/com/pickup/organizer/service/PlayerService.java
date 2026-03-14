package com.pickup.organizer.service;

import org.springframework.stereotype.Service;

import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.DuplicateEmailException;
import com.pickup.organizer.repository.PlayerRepository;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player registerPlayer(Player newPlayer) {
        if (playerRepository.findByEmail(newPlayer.getEmail()) == null) {
            playerRepository.save(newPlayer);
            return newPlayer;
        } else {
            throw new DuplicateEmailException("Email already taken!");
        }
    }
}

