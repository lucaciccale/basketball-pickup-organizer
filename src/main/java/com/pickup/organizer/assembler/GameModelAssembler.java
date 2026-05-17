package com.pickup.organizer.assembler;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.pickup.organizer.service.GameService.TERMINAL_STATUSES;
import static com.pickup.organizer.service.GameService.MIN_HRS_IN_ADVANCE;
import static com.pickup.organizer.service.GameService.MIN_MINS_IN_ADVANCE;

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
            game.getCurrentPlayers(),
            game.getMaxPlayers(),
            game.getStatus()
        );

        model.add(linkTo(methodOn(GameController.class)
            .getGame(game.getId())).withSelfRel());

        if (isJoinable(game)) {
            model.add(linkTo(methodOn(GameController.class)
                .joinGame(game.getId(), null))
                    .withRel("join"));
        }

        if (isAbleToLeave(game)) {
            model.add(linkTo(methodOn(GameController.class)
                .leaveGame(game.getId(), null))
                    .withRel("leave"));
        }

        if (isCancelable(game)) {
            model.add(linkTo(methodOn(GameController.class)
                .cancelGame(game.getId()))
                    .withRel("cancel"));
        }

        return model;
    }

    private boolean isCancelable(Game game) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        return game.getDateTime().isAfter(minAllowedTime)
            && !TERMINAL_STATUSES.contains(game.getStatus());
    }

    private boolean isJoinable(Game game) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusMinutes(MIN_MINS_IN_ADVANCE);
        return game.getDateTime().isAfter(minAllowedTime)
            && game.getStatus() == GameStatus.OPEN;
    }

    private boolean isAbleToLeave(Game game) {
        LocalDateTime minAllowedTime = LocalDateTime.now().plusHours(MIN_HRS_IN_ADVANCE);
        return game.getCurrentPlayers() != 0
            && game.getDateTime().isAfter(minAllowedTime)
            && !TERMINAL_STATUSES.contains(game.getStatus());
    }

}
