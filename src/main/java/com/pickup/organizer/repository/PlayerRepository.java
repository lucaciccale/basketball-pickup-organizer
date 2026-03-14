package com.pickup.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickup.organizer.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByEmail(String email);
}

