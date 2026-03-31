package com.pickup.organizer.controller;

import java.net.URI;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import com.pickup.organizer.assembler.PlayerModelAssembler;
import com.pickup.organizer.dto.player.PasswordUpdateDto;
import com.pickup.organizer.dto.player.PlayerUpdateDto;
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
        URI uri = linkTo(methodOn(PlayerController.class).getPlayer(player.getId())).toUri();
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

    @PutMapping("/{id}")
    public ResponseEntity<PlayerModel> replacePlayer(@PathVariable Long id, @Valid @RequestBody Player newPlayer) {
        Player player = service.replacePlayer(newPlayer, id);
        return ResponseEntity.ok(assembler.toModel(player));
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<PlayerModel> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDto dto) {
        Player player = service.updatePassword(dto, id);
        return ResponseEntity.ok(assembler.toModel(player));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlayerModel> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerUpdateDto dto) {
        Player player = service.updatePlayer(dto, id);
        return ResponseEntity.ok(assembler.toModel(player));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
