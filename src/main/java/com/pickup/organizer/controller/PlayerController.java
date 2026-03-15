package com.pickup.organizer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.pickup.organizer.entity.Player;
import com.pickup.organizer.service.PlayerService;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public Player registerPlayer(@RequestBody Player incomingPlayer) {
        return playerService.registerPlayer(incomingPlayer);
    }

    @GetMapping
    public List<Player> getPlayers() {
        return playerService.getPlayers();
    }
}

