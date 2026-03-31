package com.pickup.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickup.organizer.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
