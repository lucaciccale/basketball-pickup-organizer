package com.pickup.organizer.dto.game;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCreateDto {

    @NotBlank(message = "Location is required!")
    private String location;

    @NotNull(message = "Date and time are required!")
    @Future(message = "Must be a valid date.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @NotNull(message = "Maximum number of players is required!")
    @Min(value = 4, message = "Minimum number of players is 4 (2v2).")
    @Max(value = 10, message = "Maximum number of players is 10 (5v5).")
    private Integer maxPlayers;

}
