package com.pickup.organizer.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickup.organizer.dto.PlayerUpdateDto;
import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.DuplicateEmailException;
import com.pickup.organizer.exception.PlayerNotFoundException;
import com.pickup.organizer.repository.PlayerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;
    
    private void checkEmailUniqueness(String email, Long id) {
        Optional<Player> player = repository.findByEmail(email);
        if (player.isPresent() && !player.get().getId().equals(id)) {
            throw new DuplicateEmailException(email);
        }
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

    @Transactional
    public Player registerPlayer(Player newPlayer) {
        checkEmailUniqueness(newPlayer.getEmail(), null);
        return repository.save(newPlayer);
    }

    @Transactional
    public Player replacePlayer(Player newPlayer, Long id) {
        findPlayerById(id);
        checkEmailUniqueness(newPlayer.getEmail(), id);
        newPlayer.setId(id);
        return repository.save(newPlayer);
    }

    @Transactional
    public Player updatePlayer(PlayerUpdateDto dto, Long id) {
        Player player = findPlayerById(id); 
        if (dto.getEmail() != null) {
            checkEmailUniqueness(dto.getEmail(), id);
            player.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) player.setName(dto.getName());
        if (dto.getLastName() != null) player.setLastName(dto.getLastName());
        if (dto.getBirthDate() != null) player.setBirthDate(dto.getBirthDate());
        return repository.save(player);
    }

    @Transactional
    public Player deletePlayer(Long id) {
        Player player = findPlayerById(id);
        repository.delete(player);
        return player;
    }

}
