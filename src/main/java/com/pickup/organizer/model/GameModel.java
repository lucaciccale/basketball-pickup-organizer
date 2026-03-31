package com.pickup.organizer.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.pickup.organizer.enums.GameStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class GameModel extends RepresentationModel<GameModel> {
    
    private Long id;

    private String location;

    private LocalDateTime dateTime;

    private int maxPlayers;

    private GameStatus status;

}
