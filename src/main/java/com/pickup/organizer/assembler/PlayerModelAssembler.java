package com.pickup.organizer.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.pickup.organizer.controller.PlayerController;
import com.pickup.organizer.entity.Player;
import com.pickup.organizer.model.PlayerModel;

@Component
public class PlayerModelAssembler extends RepresentationModelAssemblerSupport<Player, PlayerModel> {

    public PlayerModelAssembler() {
        super(PlayerController.class, PlayerModel.class);
    }

    public PlayerModel toModel(Player player) {
        PlayerModel model = new PlayerModel(
            player.getId(),
            player.getName(),
            player.getLastName(),
            player.getEmail(),
            player.getBirthDate()
        );
        model.add(linkTo(methodOn(PlayerController.class).getPlayer(player.getId())).withSelfRel());
        return model;
    }
}
