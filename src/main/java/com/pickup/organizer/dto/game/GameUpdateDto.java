package com.pickup.organizer.dto.game;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdateDto {

    private String location;

    @Future(message = "Must be a valid date.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Min(value = 4, message = "Minimum number of players is 4 (2v2).")
    @Max(value = 10, message = "Maximum number of players is 10 (5v5).")
    private Integer maxPlayers;
    
}
