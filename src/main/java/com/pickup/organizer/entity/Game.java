package com.pickup.organizer.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.pickup.organizer.enums.GameStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status;

}
