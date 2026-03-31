package com.pickup.organizer.controller;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.pickup.organizer.assembler.GameModelAssembler;
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

    @GetMapping("/{id}")
    public ResponseEntity<GameModel> getGame(@PathVariable Long id) {
        Game game = service.findGameById(id);
        return ResponseEntity.ok(assembler.toModel(game));
    }
    
}
