package com.pickup.organizer.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.pickup.organizer.controller.GameController;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.model.GameModel;

public class GameModelAssembler extends RepresentationModelAssemblerSupport<Game, GameModel> {

    public GameModelAssembler() {
        super(GameController.class, GameModel.class);
    }

    public GameModel toModel(Game game) {
        GameModel model = new GameModel(
            game.getId(),
            game.getLocation(),
            game.getDateTime(),
            game.getMaxPlayers(),
            game.getStatus()
        );
        // model.add(linkTo(methodOn(GameController.class).getGame(game.getId())).withSelfRel());
        return model;
    }

}
