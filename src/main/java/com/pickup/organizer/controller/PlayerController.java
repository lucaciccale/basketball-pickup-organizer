package com.pickup.organizer.controller;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import com.pickup.organizer.assembler.PlayerModelAssembler;
import com.pickup.organizer.entity.Player;
import com.pickup.organizer.exception.PlayerNotFoundException;
import com.pickup.organizer.model.PlayerModel;
import com.pickup.organizer.service.PlayerService;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService service;
    private final PlayerModelAssembler assembler;

    @PostMapping("/register")
    public ResponseEntity<PlayerModel> registerPlayer(@Valid @RequestBody Player newPlayer) {
        Player player = service.registerPlayer(newPlayer);
        URI uri = linkTo(PlayerController.class).slash(player.getId()).toUri();
        return ResponseEntity.created(uri).body(assembler.toModel(player));
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerModel> getPlayer(@PathVariable Long id) {
        Player player = service.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id));
        return ResponseEntity.ok(assembler.toModel(player));
    }
}
