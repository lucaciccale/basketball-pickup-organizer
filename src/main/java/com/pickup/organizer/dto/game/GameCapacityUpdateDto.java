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
public class GameCapacityUpdateDto {

    @NotNull(message = "Maximum number of players is required!")
    @Min(value = 4, message = "Minimum number of players is 4 (2v2).")
    @Max(value = 10, message = "Maximum number of players is 10 (5v5).")
    private Integer maxPlayers;
    
}
