package com.pickup.organizer.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required!")
    private String name;

    @NotBlank(message = "Last name is required!")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

    @NotNull(message = "Birth date is required!")
    @Past(message = "Must be a valid birth date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
