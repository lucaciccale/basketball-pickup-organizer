package com.pickup.organizer.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pickup.organizer.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByEmail(String email);

    Page<Player> findByNameStartsWithAndBirthDateAfter(String name, LocalDate birthDate, Pageable pageable);

    Page<Player> findByNameStartsWith(String name, Pageable pageable);

    Page<Player> findByBirthDateAfter(LocalDate birthDate, Pageable pageable);

}
