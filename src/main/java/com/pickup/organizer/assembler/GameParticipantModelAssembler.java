package com.pickup.organizer.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.pickup.organizer.controller.GameController;
import com.pickup.organizer.entity.GameParticipant;
import com.pickup.organizer.model.GameParticipantModel;

@Component
public class GameParticipantModelAssembler extends RepresentationModelAssemblerSupport<GameParticipant, GameParticipantModel> {

    public GameParticipantModelAssembler() {
        super(GameController.class, GameParticipantModel.class);
    }

    public GameParticipantModel toModel(GameParticipant participant) {
        GameParticipantModel model = new GameParticipantModel(
            participant.getPlayer().getId(),
            participant.getPlayer().getName(),
            participant.getPlayer().getLastName(),
            participant.getPlayer().getEmail(),
            participant.getPlayer().getBirthDate(),
            participant.getSkillRating()
        );

        model.add(linkTo(methodOn(GameController.class)
            .getParticipant(participant.getGame().getId(), participant.getPlayer().getId()))
                .withSelfRel());
        
        return model;
    }
    
}
