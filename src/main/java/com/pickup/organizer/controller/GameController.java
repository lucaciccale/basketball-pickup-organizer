package com.pickup.organizer.controller;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import jakarta.validation.Valid;

import com.pickup.organizer.assembler.GameModelAssembler;
import com.pickup.organizer.dto.game.GameCreateDto;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.model.GameModel;
import com.pickup.organizer.service.GameService;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameController {

    private final GameService service;
    private final GameModelAssembler assembler;
    private final PagedResourcesAssembler<Game> pagedResourcesAssembler;

    @PostMapping("/create")
    public ResponseEntity<GameModel> createGame(@Valid @RequestBody GameCreateDto newGame) {
        Game game = service.createGame(newGame);
        URI uri = linkTo(methodOn(GameController.class).getGame(game.getId())).toUri();
        return ResponseEntity.created(uri).body(assembler.toModel(game));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameModel> getGame(@PathVariable Long id) {
        Game game = service.findGameById(id);
        return ResponseEntity.ok(assembler.toModel(game));
    }
    
}
