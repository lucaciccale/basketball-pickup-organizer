package com.pickup.organizer.dto.game;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinGameDto {

    @NotNull(message = "Player's ID is required!")
    @Min(value = 1, message = "Player ID must be greater than or equal to 1.")
    private Long playerId;

    @NotNull(message = "Player's skill rating is required!")
    @Min(value = 1, message = "Minimum rating is 1.")
    @Max(value = 10, message = "Maximum rating is 10.")
    private Integer skillRating;

}
