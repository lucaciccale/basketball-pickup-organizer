package com.pickup.organizer.assembler;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.pickup.organizer.service.GameService.MIN_TIME_IN_ADVANCE_HRS;

import com.pickup.organizer.controller.GameController;
import com.pickup.organizer.entity.Game;
import com.pickup.organizer.enums.GameStatus;
import com.pickup.organizer.model.GameModel;

@Component
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

        model.add(linkTo(methodOn(GameController.class)
            .getGame(game.getId())).withSelfRel());

        if (isCancellable(game)) {
            model.add(linkTo(methodOn(GameController.class)
                .cancelGame(game.getId())).withRel("cancel"));
        }

        return model;
    }

    private boolean isCancellable(Game game) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_TIME_IN_ADVANCE_HRS);
        return game.getDateTime().isAfter(minAllowedTime)
            && (game.getStatus() == GameStatus.OPEN || game.getStatus() == GameStatus.FULL);
    }

}
