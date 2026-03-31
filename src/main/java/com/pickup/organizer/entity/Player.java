package com.pickup.organizer.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required!")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must only contain letters.")
    private String name;

    @NotBlank(message = "Last name is required!")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must only contain letters.")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Must be a valid email address.")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must contain at least 8 characters.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // TODO: figure out a way of validating the birthdate correctly
    @NotNull(message = "Birth date is required!")
    @Past(message = "Must be a valid birth date.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

}
