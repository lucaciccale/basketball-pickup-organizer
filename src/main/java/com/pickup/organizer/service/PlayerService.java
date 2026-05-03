package com.pickup.organizer.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickup.organizer.dto.player.*;
import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.player.*;
import com.pickup.organizer.repository.PlayerRepository;
import com.pickup.organizer.specification.PlayerSpecifications;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;
    
    @Transactional
    public Player registerPlayer(Player newPlayer) {
        checkEmailUniqueness(newPlayer.getEmail(), null);
        return repository.save(newPlayer);
    }

    public Player findPlayerById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id));
    }
    
    public Page<Player> searchPlayers(String name, LocalDate birthDate, int page, int size) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new InvalidBirthDateException("Birth date cannot be in the future.");
        }
        Specification<Player> spec = Specification
            .where(PlayerSpecifications.nameStartsWith(name))
            .and(PlayerSpecifications.isAfter(birthDate));
        return repository.findAll(spec, PageRequest.of(page, size));
    }

    @Transactional
    public Player replacePlayer(Long id, Player newPlayer) {
        if (!repository.existsById(id)) {
            throw new PlayerNotFoundException(id);
        }
        checkEmailUniqueness(newPlayer.getEmail(), id);
        newPlayer.setId(id);
        return repository.save(newPlayer);
    }

    @Transactional
    public Player updatePassword(Long id, PasswordUpdateDto dto) {
        Player player = findPlayerById(id);
        if (!player.getPassword().equals(dto.getOldPassword())) {
            throw new IncorrectPasswordException();
        }
        if (player.getPassword().equals(dto.getNewPassword())) {
            throw new SamePasswordException();
        }
        player.setPassword(dto.getNewPassword());
        return repository.save(player);
    }

    @Transactional
    public Player updatePlayer(Long id, PlayerUpdateDto dto) {
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
    public void deletePlayer(Long id) {
        repository.delete(findPlayerById(id));
    }

    private void checkEmailUniqueness(String email, Long id) {
        boolean exists = (id == null)
            ? repository.existsByEmail(email)
            : repository.existsByEmailAndIdNot(email, id);
        if (exists) {
            throw new DuplicateEmailException(email);
        }
    }

}
