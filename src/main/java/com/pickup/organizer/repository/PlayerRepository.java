package com.pickup.organizer.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pickup.organizer.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Player> findByNameStartsWithAndBirthDateAfter(String name, LocalDate birthDate, Pageable pageable);

    Page<Player> findByNameStartsWith(String name, Pageable pageable);

    Page<Player> findByBirthDateAfter(LocalDate birthDate, Pageable pageable);

}
