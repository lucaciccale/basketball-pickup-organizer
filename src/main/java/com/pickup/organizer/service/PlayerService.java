package com.pickup.organizer.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.DuplicateEmailException;
import com.pickup.organizer.exception.PlayerNotFoundException;
import com.pickup.organizer.repository.PlayerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;

    public Player registerPlayer(Player newPlayer) {
        String email = newPlayer.getEmail();
        if (repository.findByEmail(email).isEmpty()) {
            return repository.save(newPlayer);
        }
        throw new DuplicateEmailException(email);
    }

    public Player findPlayerById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Page<Player> findPlayers(String name, LocalDate birthDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (name != null && birthDate != null) {
            return repository.findByNameStartsWithAndBirthDateAfter(name, birthDate, pageable);
        } else if (name != null) {
            return repository.findByNameStartsWith(name, pageable);
        } else if (birthDate != null) {
            return repository.findByBirthDateAfter(birthDate, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
}
