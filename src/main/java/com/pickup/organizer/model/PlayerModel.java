package com.pickup.organizer.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PlayerModel extends RepresentationModel<PlayerModel> {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
