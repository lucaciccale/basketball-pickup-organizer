package com.pickup.organizer.controller;

import java.net.URI;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import com.pickup.organizer.assembler.PlayerModelAssembler;
import com.pickup.organizer.entity.Player;
import com.pickup.organizer.model.PlayerModel;
import com.pickup.organizer.service.PlayerService;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService service;
    private final PlayerModelAssembler assembler;
    private final PagedResourcesAssembler<Player> pagedResourcesAssembler;

    @PostMapping("/register")
    public ResponseEntity<PlayerModel> registerPlayer(@Valid @RequestBody Player newPlayer) {
        Player player = service.registerPlayer(newPlayer);
        URI uri = linkTo(PlayerController.class).slash(player.getId()).toUri();
        return ResponseEntity.created(uri).body(assembler.toModel(player));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerModel> getPlayer(@PathVariable Long id) {
        Player player = service.findPlayerById(id);
        return ResponseEntity.ok(assembler.toModel(player));
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PlayerModel>> getPlayers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) LocalDate bornAfter,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "2", required = false) int size
    ) {
        Page<Player> players = service.findPlayers(name, bornAfter, page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(players, assembler));
    }
}
